package ni.vsbuild

// Use JsonSlurperClassic instead of JsonSlurper because the classic
// version uses HashMaps which are serializable. The newer version
// returns LazyMaps, which are not serializable, so can not be used
// in the Jenkins pipeline
import groovy.json.JsonSlurperClassic

class BuildConfiguration implements Serializable {

   static final String STAGING_DIR = 'staging'

   public final def archive
   public final def projects
   public final def codegen
   public final def build
   public final def test
   public final def dependencies
   public final def packageInfo

   private BuildConfiguration(archive, projects, codegen, build, test, dependencies, packageInfo) {
      this.archive = archive
      this.projects = projects
      this.codegen = codegen
      this.build = build
      this.dependencies = dependencies
      this.test = test
      this.packageInfo = packageInfo
   }

   static BuildConfiguration load(def script, String jsonFile) {
      def config = script.readJSON file: jsonFile

      // Convert the JSON to HashMaps instead of using the JsonObject
      // because the Pipeline security plugin disables lots of JsonObject
      // functionality that is required for this build system
      def convertedJson = new JsonSlurperClassic().parseText(config.toString())

      return new BuildConfiguration(
         convertedJson.archive,
         convertedJson.projects,
         convertedJson.codegen,
         convertedJson.build,
         convertedJson.test,
         convertedJson.dependencies,
         convertedJson.package)
   }

   public void printInformation(script) {
      def configurationString = """
         Build configuration is:
            Archive: $archive
            Projects: $projects
            Codegen: $codegen
            Build: $build
            Tests: $tests
            Dependencies: $dependencies
            Package: $packageInfo
         """.stripIndent()

      script.echo configurationString
   }

   public def getProjectList() {
      return getFieldList(projects)
   }

   public def getDependenciesList() {
      return getFieldList(dependencies)
   }

   private def getFieldList(def field) {
      def list = []
      for(def key : field.keySet()) {
         list.add(field.get(key))
      }

      return list
   }
}
