def call(){
   echo 'Cloning build tools to workspace.'
   
   //def organization = getComponentParts()['organization']
   def organization = env."library.vs-build-tools.github.repoOwner"
   echo organization
   def branch = env."library.vs-build-tools.version"
   buildToolsDir = cloneRepo("https://github.com/$organization/niveristand-custom-device-build-tools", branch)
   return buildToolsDir
}
