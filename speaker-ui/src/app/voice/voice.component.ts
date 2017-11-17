import { Component, OnInit, AfterViewInit } from '@angular/core';
import { RestService } from '../services/rest/rest.service'
import * as RecordRTC from 'recordrtc'

@Component({
  selector: 'app-voice',
  templateUrl: './voice.component.html',
  styleUrls: ['./voice.component.css']
})
export class VoiceComponent implements AfterViewInit{

  private recording: boolean;
  private stream: MediaStream;
  private recorder; 
  
  constructor(private restService: RestService) {
    if(typeof navigator.mediaDevices === 'undefined' || !navigator.mediaDevices.getUserMedia) {
      alert('This browser does not supports WebRTC getUserMedia API.');
    }
  }

  ngAfterViewInit() {
    this.recording = false;
    this.stream = null;
  }

  isRecording(){
    return this.recording;
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
    this.recording = true;
    var options = {
      mimeType: 'audio/wav',
      audioBitsPerSecond: 128000,
      bitsPerSecond: 128000 // if this line is provided, skip above two
    };
    this.stream = stream;
    this.recorder = RecordRTC(stream, options);
    this.recorder.startRecording();
  }

  errorCallback() {
    //handle error here
  }

  stopRecording() {
    let recordRTC = this.recorder;
    recordRTC.stopRecording(this.processVideo.bind(this));
    let stream = this.stream;
    stream.getAudioTracks().forEach(track => track.stop());
    this.recording = false;
  }


  processVideo(audioWebMURL) {
    let recordRTC = this.recorder;
    var recordedBlob = recordRTC.getBlob();
    recordRTC.getDataURL(function (dataURL) { });
  }

  releaseMicrophone(){
    this.stream.stop();
    this.stream = null;
  }

  checkable(){
    return !this.isRecording() && this.stream != null; 
  }

  authenticate() {
    this.recorder.save('audio.wav');
    this.releaseMicrophone();

    this.restService.post("");
  }
}
