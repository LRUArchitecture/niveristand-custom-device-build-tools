package ni.vsbuild.v2.stages

abstract class AbstractStage implements Stage {

   def stageName
   def script

   AbstractStage(script, String stageName) {
      this.script = script
      this.stageName = stageName
   }

   void execute() {
      script.stage("$stageName") {
         executeStage()
      }
   }
   
   abstract void executeStage()

}
