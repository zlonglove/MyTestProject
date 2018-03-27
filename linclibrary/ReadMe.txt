1.在新建的Module中编写完代码后，接下来编译整个工程后就会自动生成aar包，包的路径在新建的Module ==》 build ===》outputs ==>aar目录下


2.其他androidstudio工程引用aar包

①.将aar包复制到lib目录下

②.配置build.gradle文件：

加入

  repositories {
        flatDir {
        dirs 'libs'
    }

compile(name:'camerascan-1.0', ext:'aar')