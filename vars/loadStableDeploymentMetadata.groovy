def call(Map config = [:]) {
    def bucket = config.bucket
    def key = "test/stable_build/last_stable_deployment.json"
    def localPath = "${env.WORKSPACE}/last_stable_deployment.json"

    echo "📥 Downloading stable deployment metadata from s3://${bucket}/${key}"
    sh "aws s3 cp s3://${bucket}/${key} ${localPath}"

    def metadata = readJSON file: localPath

    if (!metadata.fe_image || !metadata.commit || !metadata.timestamp) {
        error("❌ Stable metadata file is incomplete.")
    }

    def tag = metadata.fe_image.tokenize(':')[-1]                               // Extract tag from image URL

    env.IMAGE_TAG = tag
    env.GIT_COMMIT = metadata.commit
    env.BUILD_TIMESTAMP = metadata.timestamp

    echo "✅ Stable metadata loaded. IMAGE_TAG=${env.IMAGE_TAG}, COMMIT=${env.GIT_COMMIT}"
}