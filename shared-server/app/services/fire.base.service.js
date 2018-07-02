var stream = require('stream');
var moment = require('moment');
var config = require('../../config/default')

const keyFilename   = config.firebase.keyFilename;
const projectId     = config.firebase.projectId
const bucketName    = `${projectId}.appspot.com`;
const gcs = require(config.firebase.storage)({
    projectId,
    keyFilename
});

//ISSUE: https://stackoverflow.com/questions/42879012/how-do-i-upload-a-base64-encoded-image-string-directly-to-a-google-cloud-stora
class FireBaseService{
    constructor(){
        
        this.upload = (fileData) => {
            return new Promise((resolve, reject) => {
                var bufferStream = new stream.PassThrough();
                bufferStream.end(new Buffer(fileData, 'base64'));
                var path = makeFilePath();
                var bucket = gcs.bucket(bucketName);
                var file = bucket.file(path);
                bufferStream.pipe(
                    file.createWriteStream({
                        metadata: {
                            contentType: 'image/jpeg',
                            metadata: {
                                custom: 'metadata'
                            }
                        },
                        public: true,
                        destination: path,
                        validation: "md5"
                    })
                )
                .on('error', function(err) {
                    console.log("error uploading:",err);
                    reject(err);
                })
                .on('finish', function() {
                    var url = file.metadata.mediaLink;
                    var size = file.metadata.size;
                    var data = {
                        url: url,
                        size: size
                    };
                    resolve(data);
                });
            });
        };

        function makeFilePath(fileName) {
            var currentDate = moment().toDate().getTime();
            var fileName = currentDate + '_image';
            return 'test/' + fileName;
        }

        this.getResourceUrl = function(fileId) {

            return new Promise((resolve, reject) => {
                var filePath = config.firebase.baseDirectory + fileId;
                // var filePath = 'test/1530488160340_image';
                // var filePath = 'media/ff781bc295614baf8d6bc0743242a1bf';
                const options = {
                    action: 'read',
                    expires: '03-17-2025',
                };    

                gcs.bucket(bucketName)
                .file(filePath)
                .getSignedUrl(options)
                .then(results => {
                    const url = results[0];
                    resolve(url)
                })
                .catch(err => {
                    console.error('ERROR:', err);
                    reject(err);
                });
            })
        }
    }
}

module.exports = new FireBaseService();
