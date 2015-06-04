package vcs.bglTFConverter

import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import javax.imageio.ImageIO

public class BinaryItem {
	String oldId
	String uri
	Path path
	String bufferView;
	String mimetype;
	String name;
	String type;
	int byteLength;
	def bytes;
	public BinaryItem(Path path, String oldId, String uri, String type, int byteLength){
		this.path = path;
		this.oldId = oldId;
		this.uri = uri;		
		this.type = type;
		this.byteLength = byteLength;
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
			this.bytes = Files.readAllBytes(Paths.get(path.toString() + java.io.File.separator + this.uri));
		}
		return this.bytes;			
	}	
	def getName(counter){
		if(!this.name){
			this.name = "bin_" + counter;
		}
		return this.name;
	}
	def getBufferView(offset){		
		return [buffer:"CESIUM_binary_glTF", "byteLength":getByteLength(), "byteOffset":offset, target:34962]				
	}
	def getJson(){
		return ["extensions":["CESIUM_binary_glTF":["bufferView":this.name, "mimeType":this.mimetype, "height":this.height, "width":this.width]]]
	}

}