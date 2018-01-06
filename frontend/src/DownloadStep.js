import React from 'react';
import { Button, Glyphicon } from 'react-bootstrap';

export default function DownloadStep({downloadLink}) {
    return (
      <div>
          <h1 className="text-center">Done!</h1>
          <Button
            bsStyle="success"
            bsSize="large"
            block
            href={downloadLink}
            target="_blank"
          >
              Download! <Glyphicon glyph="download"/>
          </Button>
          <Button
            bsStyle="default"
            bsSize="large"
            block
            onClick={() => window.location.reload()}
          >
              <Glyphicon glyph="arrow-left"/> Download more files...
          </Button>
      </div>
    )
}
