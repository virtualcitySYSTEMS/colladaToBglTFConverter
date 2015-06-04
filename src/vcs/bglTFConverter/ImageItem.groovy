package vcs.bglTFConverter

import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import javax.imageio.ImageIO

public class ImageItem {
	String oldId
	String uri
	int width
	int height
	Path path
	String bufferView;
	String mimetype;
	String name;
	def bytes;
	public ImageItem(Path path, String oldId, String uri){
		this.path = path;
		this.oldId = oldId;
		this.uri = uri;		
		if(uri.endsWith("jpeg") || uri.endsWith("jpg")){
			mimetype = "image/jpeg"
		}else if(uri.endsWith("png")){
			mimetype = "image/png"
		}
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
			try{
				this.bytes = Files.readAllBytes(Paths.get(path.toString() + java.io.File.separator + this.uri));
			}catch(Exception Ex){
				println("Error in : " + path.toString() + java.io.File.separator + this.uri);
				this.bytes = new byte[0];
			}			
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
		return [buffer:"CESIUM_binary_glTF", "byteLength":getByteLength(), "byteOffset":offset, target:34962]				
	}
	def getJson(){
		return ["extensions":["CESIUM_binary_glTF":["bufferView":this.name, "mimeType":this.mimetype, "height":this.height, "width":this.width]]]
	}
	public void getImage(){
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path.toString() + java.io.File.separator + this.uri ));
			this.width = img.width
			this.height = img.height
			
			//println(this.width + " : " +  this.height);
		} catch (Exception e) {
			println("Error in : " + path.toString() + java.io.File.separator + this.uri);
		}
		
	}
}