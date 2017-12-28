export function getThumbnailLink(movieLink) {
    return new Promise(resolve => {
        setTimeout(() => resolve("https://i.ytimg.com/vi/gGQ2xKSF5VA/maxresdefault.jpg"), 1000);
    })
}

export function createDownloadJob(movieLink) {
    return new Promise(resolve => {
        setTimeout(() => resolve({
            id: 1
        }), 2000);
    })
}

export function getDownloadJob(id) {
    return new Promise(resolve => {
        setTimeout(() => resolve({
            id: 1,
            url: "https://www.youtube.com/watch?v=gGQ2xKSF5VA",
            filename: "/tmp/abcd.mp4",
            status: 'SUCCESS'
        }), 2000)
    })
}
