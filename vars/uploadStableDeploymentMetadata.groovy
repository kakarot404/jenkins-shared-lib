def call(Map config) {
    def metadata = [
        fe_image : config.feImage,
        be_image : config.beImage,
        commit   : config.commit,
        timestamp: config.timestamp
    ]

    def metadataFile = "${env.WORKSPACE}/last_stable_deployment.json"

    writeJSON file: metadataFile, json: metadata, pretty: 4

    echo "Uploading stable deployment metadata to s3://${config.bucket}/test/stable_build/last_stable_deployment.json"

    retry(3) {
        sh "aws s3 cp ${metadataFile} s3://${config.bucket}/test/stable_build/last_stable_deployment.json"
    }

    echo "Stable Metadata uploaded successfully."
}