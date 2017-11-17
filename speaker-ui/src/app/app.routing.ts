import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { EnrollComponent } from './enroll/enroll.component';
import { VoiceComponent } from './voice/voice.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: "/voice",
    pathMatch: 'full'
  },
  { path: 'voice', component: VoiceComponent },
  { path: 'enroll', component: EnrollComponent }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [ RouterModule ],
  declarations: []
})
export class AppRouting { }
