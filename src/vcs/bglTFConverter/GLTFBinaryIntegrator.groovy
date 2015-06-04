package vcs.bglTFConverter

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

public class GLTFBinaryIntegrator {
  
	// Path to Folder
	Path path 
	
	
	int counter = 0;
	// always start with 16 as offset for header
	int offset = 20;
	//always start with 16 as size for header
	int bufferSize = 20;
	
	List<BinaryItem> binaries = new ArrayList<BinaryItem>()
	List<ImageItem> images = new ArrayList<ImageItem>()
	List<ShaderItem> shader = new ArrayList<ShaderItem>()
	
	void doIntegration(String gltfFileName, String binaryFilePath, String binaryFileName){		
		Path gltfPath = Paths.get(gltfFileName);
		path = gltfPath.parent;
		
		def json_gltf = parseGLTFOrig(gltfFileName)

		// parse binary bin file
		parseBinaries(json_gltf);
		
		// images
		// parse Images // and change json
		parseImages(json_gltf)
		
		// parse Shaders // and change json
		parseShaders(json_gltf)

		

		
		// add self Buffer to JSON
		json_gltf.put("buffers",["CESIUM_binary_glTF":["type":"arraybuffer"]]);
		
		
		
		
		def jsonText = JsonOutput.toJson(json_gltf);
		
		//println(JsonOutput.prettyPrint(jsonText));
		def jsonBytes = jsonText.getBytes("UTF-8");
		def jsonBytesLength = jsonBytes.length;
		this.bufferSize += jsonBytesLength;
		
		ByteBuffer buffer = ByteBuffer.allocate(this.bufferSize);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		String magic = "glTF";
		buffer.put(magic.getBytes("ASCII"));
		// version
		buffer.putInt(1);
		// length all
		buffer.putInt(this.offset + jsonBytesLength);		
		// jsonOffset
		buffer.putInt(this.offset);
		// jsonLength;
		buffer.putInt(jsonBytesLength);
		binaries.each{
			buffer.put(it.getBytes());
		}
		images.each{
			buffer.put(it.getBytes());
		}
		shader.each{
			buffer.put(it.getBytes());
		}

		buffer.put(jsonBytes);
		//String binaryFilename =  gltfFileName[0..-5] + "bgltf";
		//println(binaryFilename);
		this.writeSmallBinaryFile(buffer.array(),binaryFilePath, binaryFileName );
	}
	void writeSmallBinaryFile(byte[] aBytes, String aDirectoryName, String aFileName) throws IOException {
		Path path = Paths.get(aDirectoryName);
		try{
			Files.createDirectories(path);
		}catch(FileAlreadyExistsException){
			// 	do nothing, we just overwrite the file
		}
		path = Paths.get(aDirectoryName + aFileName);
		Files.write(path, aBytes); //creates, overwrites
	  }
	def parseGLTFOrig(String filename){
		String gltf = getStringFromFile(filename);
		JsonSlurper slurper = new JsonSlurper();
		return slurper.parseText(gltf);
	}
	def parseImages(json){
		json.images.each{ Object object ->
			ImageItem item = new ImageItem(path, object.key, object.value.uri);
			item.getImage();
			images.add(item);
		}		
		images.each{ ImageItem item ->
			json.bufferViews.put(item.getName(++this.counter), item.getBufferView(this.offset));
			this.offset += item.getByteLength();
			this.bufferSize += item.getByteLength();
			json.images.put(item.oldId, item.getJson())
		}
	}
	def parseShaders(json){
		json.shaders.each{ Object object ->
			ShaderItem item = new ShaderItem(path, object.key, object.value.uri, object.value.type);
			shader.add(item);
		}
		shader.each{ ShaderItem item ->
			json.bufferViews.put(item.getName(++this.counter), item.getBufferView(this.offset));
			this.offset += item.getByteLength();
			this.bufferSize += item.getByteLength();
			json.shaders.put(item.oldId, item.getJson())
		}
	}
	def parseBinaries(json){
		json.buffers.each{ Object object ->
			BinaryItem item = new BinaryItem(path, object.key, object.value.uri, object.value.type, object.value.byteLength);
			binaries.add(item);
		}
		
		binaries.each{ BinaryItem item ->
			def name = item.getName(++this.counter);
			def oldId = item.getOldId();
			int binOffset = this.offset;
			json.bufferViews.each{ Object object ->
				if(object.value.buffer == oldId){
					object.value.buffer = "CESIUM_binary_glTF";
					object.value.byteOffset = binOffset
					binOffset += object.value.byteLength 					
				}
			}
			//json.bufferViews.put(item.getName(++this.counter), item.getBufferView(this.offset));
			this.offset += item.getByteLength();
			this.bufferSize += item.getByteLength();
			//json.shaders.put(item.oldId, item.getJson())
		}
	}
	
	String getStringFromFile(String fileName){
		Path path  = Paths.get(fileName);
		List<String> stringList = Files.readAllLines(path, StandardCharsets.UTF_8);
		String string = "";
		for(int i = 0; i < stringList.size(); i++){
			string += stringList.get(i);
		}
		return string;
	}
}