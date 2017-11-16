import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-voice',
  templateUrl: './voice.component.html',
  styleUrls: ['./voice.component.css']
})
export class VoiceComponent implements OnInit {
  
  private recorder; 
  private microphone;
  private audio : HTMLAudioElement;

  private stream: MediaStream;
  private recordRTC: any;
  private isEdge = navigator.userAgent.indexOf('Edge') !== -1 && (!!navigator.msSaveOrOpenBlob || !!navigator.msSaveBlob);

  constructor() { }

  ngOnInit() {

    if(typeof navigator.mediaDevices === 'undefined' || !navigator.mediaDevices.getUserMedia) {
      alert('This browser does not supports WebRTC getUserMedia API.');

      if(!!navigator.getUserMedia) {
          alert('This browser seems supporting deprecated getUserMedia API.');
      }
    }
    this.audio = document.querySelector('audio');
  }

  replaceAudio(src: any) {

    var newAudio = document.createElement('audio');
    newAudio.controls = true;

    if(src) {
        newAudio.src = src;
    }
    
    var parentNode = this.audio.parentNode;
    parentNode.textContent = '';
    parentNode.appendChild(newAudio);

    this.audio = newAudio;
}

  startRecording(){
    let mediaConstraints = {
      video: false, audio: true
    };
    navigator.mediaDevices
      .getUserMedia(mediaConstraints)
      .then(this.successCallback.bind(this), this.errorCallback.bind(this));
  }

  successCallback(stream: MediaStream) {

    this.replaceAudio(null);
    
    this.audio.muted = true;
    this.setSrcObject(microphone, audio);
    this.audio.play();
    
    var options = {
        type: 'audio',
        numberOfAudioChannels: this.isEdge ? 1 : 2,
        checkForInactiveTracks: true,
        bufferSize: 16384
    };

    if(navigator.platform && navigator.platform.toString().toLowerCase().indexOf('win') === -1) {
        options.sampleRate = 48000; // or 44100 or remove this line for default
    }
    
    var recorder = RecordRTC(microphone, options);
    
    recorder.startRecording();
    
    document.getElementById('btn-stop-recording').disabled = false;
  }

  errorCallback() {
    //handle error here
  }

  stopRecording(){

  }

  download(){

  }
}
