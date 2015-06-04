# colladaToBGLTFConverter

colladaToBglTFConverter, is a small groovy/Java script to convert collada datasets to the binary-glTF Format used in Cesium 1.10. 
See http://cesiumjs.org/2015/06/01/Binary-glTF/. The script finds all "dae" files in the input folder and passes the file to the 
collada2gltf.exe from https://github.com/KhronosGroup/glTF to convert the collada file to glTF. In a post processing step the gltf 
dataset is converted to a binaryglTF file and written to the output folder. 

# Download

Just download the colladaToBglTFConverter.jar file from https://github.com/virtualcitySYSTEMS/colladaToBglTFConverter/releases
And the collada2gltf.exe from https://github.com/KhronosGroup/glTF/releases


# Usage

java -jar colladaToBglTFConverter -c collada2gltf.exe -i inputPath -o outputPath




