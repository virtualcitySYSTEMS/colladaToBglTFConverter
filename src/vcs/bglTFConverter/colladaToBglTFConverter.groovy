package vcs.bglTFConverter

import groovyx.gpars.GParsPool

import java.util.concurrent.atomic.AtomicInteger

class colladaToBglTFConverter {

	static main(args) {
		
		def parentfiles = [:]
		def files = []
		def collada2gltfTool = "D:\\WorkspaceCesium\\iterator\\lib\\collada2gltf.exe"
		
		new File('D:\\temp\\cesium\\exported_neu').eachFileRecurse { File file ->
			if(file.name.count('.dae')){
				files << file
			}
		}
			
			
		def counter = 0;
		def percent = 0;
		def fileCount = files.size();
		def steps = fileCount /100;
		println("Converting " + fileCount + " dae files");		
		jsr166y.ForkJoinPool currentPool
		GParsPool.withPool 4,{ pool ->
			currentPool = pool;
			files.eachParallel { File file->				
				def command = collada2gltfTool + ' -r -f ' + file.name + ' -o ' + file.name[0..-4] + 'gltf'
				def proc = command.execute([], new File(file.parent))
				proc.waitFor();				
				try{
					GLTFBinaryIntegrator integrator = new GLTFBinaryIntegrator();
					integrator.doIntegration(file.canonicalPath[0..-4] + 'gltf');
				}catch(Exception Ex){
					println(file.name)
					println(file.parent);
					println(Ex.getMessage());
					pool.shutdownNow();
				}			
				synchronized(this){	
					counter++;				
					if(counter > steps){
						percent++;					
						println(percent + "%");
						counter = 0;
					}
				}				
			}	
		}
		println("finished");
	}
}
