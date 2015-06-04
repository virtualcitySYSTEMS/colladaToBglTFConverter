# colladaToBGLTFConverter
=====

colladaToBglTFConverter is a small groovy/JavaScript tool to convert COLLADA data sets into the binary-glTF Format that is used, for example, in Cesium 1.10. 
Follow this [link](http://cesiumjs.org/2015/06/01/Binary-glTF/) for more details.


0. Index
--------

1. License
2. About
3. System requirements
5. How To
8. Developers
9. Contact
11. Disclaimer


1. License
----------

The scripts for colladaToBGLTFConverter are open source under Apache 2.0 license.
See the file LICENSE for more details. 


2. About
----------

The script looks for all "dae" files in a given input folder and passes the files to the [collada2gltf.exe](https://github.com/KhronosGroup/glTF) to convert the COLLADA files to glTF. 
In a post processing step the glTF data set is converted to a binary glTF file and written to a given output folder. 


3. System requirements
----------------------

* Java RE 1.7 or higher.


4. How To
----------

First download the necessary tools:
* colladaToBglTFConverter.jar from https://github.com/virtualcitySYSTEMS/colladaToBglTFConverter/releases
* collada2gltf.exe from https://github.com/KhronosGroup/glTF/wiki/Converter-builds

Then, run the following from CLI:
<pre>java -jar colladaToBglTFConverter.jar -c collada2gltf.exe -i inputPath -o outputPath</pre>


5. Developers
-------------

Jannes Bolling


6. Contact
----------

jbolling@virtualcitysystems.de


7. Disclaimer
--------------

THIS SOFTWARE IS PROVIDED BY virtualcitySYSTEMS GmbH "AS IS" AND "WITH ALL 
FAULTS." virtualcitySYSTEMS GmbH MAKES NO REPRESENTATIONS OR WARRANTIES OF 
ANY KIND CONCERNING THE QUALITY, SAFETY OR SUITABILITY OF THE SOFTWARE,
EITHER EXPRESSED OR IMPLIED, INCLUDING WITHOUT LIMITATION ANY IMPLIED 
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR 
NON-INFRINGEMENT.

virtualcitySYSTEMS GmbH MAKES NO REPRESENTATIONS OR WARRANTIES AS TO THE
TRUTH, ACCURACY OR COMPLETENESS OF ANY STATEMENTS, INFORMATION OR MATERIALS
CONCERNING THE SOFTWARE THAT IS CONTAINED ON AND WITHIN ANY OF THE 
WEBSITES OWNED AND OPERATED BY virtualcitySYSTEMS GmbH.

IN NO EVENT WILL virtualcitySYSTEMS GmbH BE LIABLE FOR ANY INDIRECT, 
PUNITIVE, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES HOWEVER THEY MAY
ARISE AND EVEN IF virtualcitySYSTEMS GmbH HAVE BEEN PREVIOUSLY ADVISED OF
THE POSSIBILITY OF SUCH DAMAGES.