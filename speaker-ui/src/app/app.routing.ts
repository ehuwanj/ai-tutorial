import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { EnrollComponent } from './enroll/enroll.component';
import { AboutComponent } from "./about/about.component";

const routes: Routes = [
  { path: 'enroll', component: EnrollComponent },
  { path: 'about', component: AboutComponent }
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
