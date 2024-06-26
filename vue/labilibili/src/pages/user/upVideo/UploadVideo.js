// 处理上传


/**
 * 断点续传
 */
import { ref } from 'vue';
import axios from 'axios';

const file = ref(null);
const uploadProgress = ref(0);
const CHUNK_SIZE = 1024 * 1024; // 1MB
const uploadUrl = 'http://example.com/upload';

const handleFileChange = (event) => {
  file.value = event.target.files[0];
  uploadProgress.value = 0;
};

const uploadFile = async () => {
  if (!file.value) return;

  const totalSize = file.value.size;
  let uploadedSize = 0;

  while (uploadedSize < totalSize) {
    const chunk = file.value.slice(uploadedSize, uploadedSize + CHUNK_SIZE);
    const headers = {
      'Content-Range': `bytes ${uploadedSize}-${uploadedSize + chunk.size - 1}/${totalSize}`
    };

    try {
      const response = await axios.put(uploadUrl, chunk, { headers });
      if (response.status === 200 || response.status === 206) {
        uploadedSize += chunk.size;
        uploadProgress.value = Math.floor((uploadedSize / totalSize) * 100);
      } else {
        console.error('Upload failed', response);
        break;
      }
    } catch (error) {
      console.error('Error during upload', error);
      break;
    }
  }

  if (uploadedSize === totalSize) {
    console.log('File uploaded successfully');
  }
}

/**
 * 上传的处理
 */