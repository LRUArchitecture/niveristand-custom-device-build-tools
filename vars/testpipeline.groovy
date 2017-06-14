#!/usr/bin/env groovy

def call(repo){
  echo 'Starting the pipeline build...'
  
  node('dcafbuild01'){
    checkoutComponent(repo, env.BRANCH_NAME)
    
    echo 'Loading component build steps...'
    def buildSteps = load 'vars/build.groovy'
    
    stage('Clean'){
      echo 'Cleaning'
      deleteDir()
      bat 'set'
    }
    stage('Checkout'){
      echo 'Attempting to get source from repo...'
      timeout(time: 5, unit: 'MINUTES'){
        checkout scm
      }
    }
    stage('Build'){
      echo 'Starting build...'
      buildSteps.call()
    }
    stage('Cleanup'){
      echo 'Cleaning up workspace...'
      deleteDir()
    }
  }
}
