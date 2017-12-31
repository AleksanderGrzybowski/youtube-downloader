import backendUrl from './../backendUrl';
import axios from 'axios';

export function getThumbnailLink(movieLink) {
    const params = new URLSearchParams();
    params.append('url', movieLink);

    return axios.get(`${backendUrl}/api/thumbnailUrl`, {params})
      .then(({data}) => data.thumbnailUrl);
}

export function createDownloadJob(youtubeUrl) {
    return axios.post(`${backendUrl}/api/jobs`, {youtubeUrl})
      .then(({data}) => data);
}

export function getDownloadJob(id) {
    return axios.get(`${backendUrl}/api/jobs/${id}`)
      .then(({data}) => data);
}

export function getDownloadLink(id) {
    return `${backendUrl}/api/jobs/${id}/download`;
}