import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import { RestService } from './services/rest/rest.service'

import { AppComponent } from './app.component';
import { AppRouting } from './app.routing';
import { VoiceComponent } from './voice/voice.component';
import { EnrollComponent } from './enroll/enroll.component';
import { VideoComponent } from './video/video.component';


@NgModule({
  declarations: [
    AppComponent,
    VoiceComponent,
    EnrollComponent,
    VideoComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRouting
  ],
  providers: [RestService],
  bootstrap: [AppComponent]
})
export class AppModule { }
