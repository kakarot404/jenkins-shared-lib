def call(Map config = [:]) {
    def bucket = config.bucket
    def key = config.key ?: "test/builds/latest_build.json"
    def localPath = "${env.WORKSPACE}/latest_build.json"

    echo "Downloading build metadata from s3://${bucket}/${key}"
    sh "aws s3 cp s3://${bucket}/${key} ${localPath}"

    def metadata = readJSON file: localPath

    echo "✅ Metadata Loaded: ${metadata}"

    // Export to env so pipeline can use
    env.IMAGE_TAG = metadata.image_tag
    env.GIT_COMMIT = metadata.commit
    env.BUILD_TIMESTAMP = metadata.timestamp
}