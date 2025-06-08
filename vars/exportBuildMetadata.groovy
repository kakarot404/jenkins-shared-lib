def call(Map args) {
    def imageTag = args.imageTag
    def commit = args.commit
    def timestamp = args.timestamp
    def bucket = args.bucket

    if (!imageTag || !commit || !timestamp || !bucket) {
        error "Missing required argument in exportBuildMetadata."
    }

    def metadata = [
        image_tag: imageTag,
        commit   : commit,
        timestamp: timestamp
    ]

    def metadataPath = "${env.WORKSPACE}/.ci/latest_build.json"

    writeJSON file: metadataPath, json: metadata, pretty: 4

    echo "Uploading metadata to s3://${bucket}/test/builds/latest_build.json"

    retry(3) {
        sh "aws s3 cp ${metadataPath} s3://${bucket}/test/builds/latest_build.json"
    }

    echo "✅ Metadata exported and uploaded successfully."
}