package iterator

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

public class ShaderItem {
	String oldId
	String uri

	Path path
	String bufferView;
	String mimetype;
	String name;
	int type;
	def bytes;
	public ShaderItem(Path path, String oldId, String uri, int type){
		this.path = path;
		this.oldId = oldId;
		this.uri = uri;		
		this.type = type;
	}
	
	
	public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getByteLength(){
		if(!this.bytes){
			getBytes();
		}
		this.bytes.length;
	}
	byte[] getBytes(){
		if(!this.bytes){
			def string = getStringFromFile(path.toString() + java.io.File.separator + this.uri);
			this.bytes = string.getBytes("UTF-8");
		}
		return this.bytes;			
	}	
	def getName(counter){
		if(!this.name){
			this.name = "tex_" + counter;
		}
		return this.name;
	}
	def getBufferView(offset){		
		return [buffer:"CESIUM_binary_glTF", "byteLength":getByteLength(), "byteOffset":offset, target:this.type]				
	}
	def getJson(){
		return ["extensions":["CESIUM_binary_glTF":["bufferView":this.name]]]
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