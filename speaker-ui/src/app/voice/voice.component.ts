import { Component, OnInit } from '@angular/core';
// import * as RecordRTC from '../../assets/scripts/RecordRTC.js';

@Component({
  selector: 'app-voice',
  templateUrl: './voice.component.html',
  styleUrls: ['./voice.component.css']
})
export class VoiceComponent implements OnInit {

  private recordingFlag: boolean;
  private recorder; 
  private microphone;
  private audio;

  private isEdge;

  constructor() { }

  ngOnInit() {

    if(typeof navigator.mediaDevices === 'undefined' || !navigator.mediaDevices.getUserMedia) {
      alert('This browser does not supports WebRTC getUserMedia API.');

      if(!!navigator.getUserMedia) {
          alert('This browser seems supporting deprecated getUserMedia API.');
      }
    }
    this.audio = document.querySelector('audio');
    this.isEdge = navigator.userAgent.indexOf('Edge') !== -1 && (!!navigator.msSaveOrOpenBlob || !!navigator.msSaveBlob);
  }

  isRecording(){
    return this.recordingFlag;
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
      .then(this.startRecordingCallback.bind(this), this.errorCallback.bind(this));
  }

  startRecordingCallback(stream: MediaStream) {

    this.recordingFlag = true;
    this.replaceAudio(null);
    
    this.audio.muted = true;

    //this.setSrcObject(microphone, audio);
    this.audio.play();
    
    var options = {
        type: 'audio',
        numberOfAudioChannels: this.isEdge ? 1 : 2,
        checkForInactiveTracks: true,
        bufferSize: 16384
    };

     this.recorder = RecordRTC(this.microphone, options);
      
    this.recorder.startRecording();
  }

  errorCallback() {
    //handle error here
  }

  stopRecording(){
    this.recorder.stopRecording(this.stopRecordingCallback);
  }

  stopRecordingCallback(){

    this.replaceAudio(URL.createObjectURL(this.recorder.getBlob()));
    this.recorder.destroy();
    this.recorder = null;
    this.recordingFlag = false;
    this.audio.play();
  }

  releaseMicrophone(){

    this.microphone.stop();
    this.microphone = null;
  }

  download(){

  }
}
