const mockDelay = 1000;

const sampleThumbnailUrl = 'https://i.ytimg.com/vi/gGQ2xKSF5VA/maxresdefault.jpg';
const healthcheckOkResponse = {status: 'UP'};
const createdDownloadJob = {id: 1};
const finishedDownloadJob = {
    id: 1,
    url: 'https://www.youtube.com/watch?v=gGQ2xKSF5VA',
    filename: '/tmp/abcd.mp4',
    status: 'SUCCESS'
};
const mockDownloadLink = '/mockDownloadLink';

// noinspection JSUnusedGlobalSymbols
export function healthcheck() {
    return new Promise(resolve => setTimeout(() => resolve(healthcheckOkResponse), mockDelay))
}

// noinspection JSUnusedGlobalSymbols
export function getThumbnailLink() {
    return new Promise(resolve => setTimeout(() => resolve(sampleThumbnailUrl), mockDelay))
}

// noinspection JSUnusedGlobalSymbols
export function createDownloadJob() {
    return new Promise(resolve => setTimeout(() => resolve(createdDownloadJob), mockDelay));
}

// noinspection JSUnusedGlobalSymbols
export function getDownloadJob() {
    return new Promise(resolve => setTimeout(() => resolve(finishedDownloadJob), mockDelay))
}

// noinspection JSUnusedGlobalSymbols
export function getDownloadLink() {
    return mockDownloadLink;
}
