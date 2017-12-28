import React, { Component } from 'react';

export default class DownloadStep extends Component {

    render() {
        return (
          <div>
              <h3>Done! Click <a href={this.props.downloadLink} target="_blank">here</a> to download your file. </h3>
          </div>
        )
    }
}
