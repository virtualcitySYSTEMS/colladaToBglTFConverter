package vcs.bglTFConverter

import groovyx.gpars.GParsPool

import java.nio.file.Paths

class colladaToBglTFConverter {

	public static void main(String[] args) {		
		def cli = new CliBuilder(usage: '"java -jar colladaToBgltfConverter');		
        cli.h(longOpt:'help','usage information',required:false);  
        cli.i(longOpt:'input','input folder containing the collada files',required: true,args:1)
		cli.o(longOpt:'output','output folder to write bgltf files',required: true,args:1)
		cli.c(longOpt:'converter','path to collada2gltf.exe',required: true,args:1)
        OptionAccessor opt = cli.parse(args)  
        if(!opt) {  
            return  
        }  
        if(opt.h || opt.arguments().isEmpty()) {  
            cli.usage()  
        }  
        		
		
		def parentfiles = [:]
		def files = []
		def collada2gltfTool = opt.c
		def filePath = opt.i
		new File(filePath).eachFileRecurse { File file ->
			if(file.name.count('.dae')){
				files << file
			}
		}
			
		def outputPath = Paths.get(opt.o);
		
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
					
					def parentFile = file.parentFile.parentFile.parentFile;					
					def resolvedFile = parentFile.toURI().relativize(file.parentFile.toURI());
					
					def outpath = outputPath.resolve(resolvedFile.toString());
									
					
					integrator.doIntegration(file.canonicalPath[0..-4] + 'gltf',outpath.toString() + java.io.File.separator, file.name[0..-4] + 'bgltf');
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
