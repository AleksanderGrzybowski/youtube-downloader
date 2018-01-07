import axios from 'axios';

// you can override for quick testing
const backendUrl = '';

export function healthcheck() {
    return axios.get(`${backendUrl}/api/health`)
      .then(({data}) => data);
}

export function getThumbnailLink(movieLink) {
    const params = new URLSearchParams();
    params.append('url', movieLink);

    return axios.get(`${backendUrl}/api/thumbnailUrl`, {params})
      .then(({data}) => data.thumbnailUrl);
}

export function createDownloadJob(youtubeUrl, type) {
    return axios.post(`${backendUrl}/api/jobs`, {youtubeUrl, type: type.toUpperCase()})
      .then(({data}) => data);
}

export function getDownloadJob(id) {
    return axios.get(`${backendUrl}/api/jobs/${id}`)
      .then(({data}) => data);
}

export function getDownloadLink(id) {
    return `${backendUrl}/api/jobs/${id}/download`;
}
