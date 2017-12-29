import React from 'react';

export default function DownloadStep({downloadLink}) {
    return (
      <div>
          <h1 className="text-center">
              Done! Click <a href={downloadLink} target="_blank">here</a> to download your file. 
          </h1>
      </div>
    )
}
