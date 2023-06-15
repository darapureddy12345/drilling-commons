#!/usr/bin/env groovy

// Define DevCloud Artifactory for publishing non-docker image artifacts
def artUploadServer = Artifactory.server('build.ge')

// Change Snapshot to your own DevCloud Artifactory repo name
def Snapshot = 'DCEXP'

pipeline {
    agent none
    environment {
        COMPLIANCEENABLED = true
    }
    options {
        skipDefaultCheckout()
        buildDiscarder(logRotator(artifactDaysToKeepStr: '1', artifactNumToKeepStr: '1', daysToKeepStr: '5', numToKeepStr: '10'))
    }
    stages {

        stage('Build') {
            agent {
                docker {
                    image 'maven:3.6.3-jdk-11'
                    label 'dind'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                checkout scm
                script {
                	final String url = "https://api.github.com/orgs/engagePlatform/packages/maven/com.bh.drillingcommons"
                    final String response = sh(script: "curl -X DELETE -u ${env.GIT_USER}:${env.GIT_TOKEN} -s $url", returnStdout: true).trim()
                    echo response
                    env.gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
                    echo "Commit ID: ${gitCommit}"
                }
                sh 'mvn -B -s settings.xml -DskipTests clean install'
                stash includes: 'target/*.jar', name: 'artifact'
            }
            post {
                always {
                    deleteDir()
                }
                success {
                    echo "Build stage completed"
                }
                failure {
                    echo "Build stage failed"
                }
            }
        }

        stage('Publish Artifacts') {
            agent {
                docker {
                    image 'maven:3.6.3-jdk-11'
                    label 'dind'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            when {
                not{
                    branch "PR-*"
                }
            } 
            steps {
                checkout scm
                sh 'mvn deploy -s settings.xml -DskipTests'
            }
            post {
                success {
                    echo "Publish Artifacts stage completed"
                }
                failure {
                    echo "Publish Artifacts stage failed"
                }
                always {
                    deleteDir()
                }
            }
        }

        stage('Unit Tests') {
            agent {
                docker {
                    image 'maven:3.6.3-jdk-11'
                    label 'dind'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                checkout scm
                sh 'mvn -B -s settings.xml -Dmaven.test.failure.ignore -Dsurefire.useSystemClassLoader=false test'
            }
            post {
                success {
                    script {
                        echo 'STARTING SUREFIRE TEST'
                        sh 'mvn -B -s settings.xml surefire-report:report-only'
                    }
                    echo "Unit Tests stage completed"
                    deleteDir()
                }
                failure {
                    echo "Unit Tests stage failed"
                    deleteDir()
                }
            }
        }

        stage('SonarQube Scan') {
            agent {
                docker {
                    image 'maven:3.6.3-jdk-11'
                    label 'dind'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            when {
                not{
                    branch "PR-*"
                }
            } 
            steps {
                checkout scm
                withSonarQubeEnv('EngageDrilling-SQ') {
                    // requires SonarQube Scanner for Maven 3.2+
                    sh 'mvn -s settings.xml org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar -Dsonar.java.binaries=target/* '
                }
            }
            post {
                success {
                    echo "sonarqube-scan stage completed"
                }
                failure {
                    echo "sonarqube-scan stage failed"
                }
                always {
                    deleteDir()
                }
            }
        }


    }

    post {
        success {
            echo 'Your Gradle pipeline was successful sending notification'
            emailext (
                    subject: 'Propel Gradle Pipeline Build Success',
                    body: "Your pipeline: '${env.JOB_NAME} [${env.BUILD_NUMBER}]': was successful. Check console output at ${env.BUILD_URL}/console}",
                    to: 'shubhankar.sarkar@bakerhughes.com'
            )
        }
        failure {
            echo "Your Gradle pipeline failed sending notification...."
            emailext (
                    subject: 'Propel Gradle Pipeline Build Failure',
                    body: "Your pipeline: '${env.JOB_NAME} [${env.BUILD_NUMBER}]': has an error. Check console output at ${env.BUILD_URL}/console}",
                    to: 'shubhankar.sarkar@bakerhughes.com,'
            )
        }

    }
}
