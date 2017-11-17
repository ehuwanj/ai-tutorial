import { Injectable } from '@angular/core';
import { RequestOptions, ResponseContentType } from '@angular/http';
import { HttpClient, HttpRequest, HttpHeaders } from '@angular/common/http';

@Injectable()
export class RestService {

  private BASE_URI = 'http://localhost:8080/';

  constructor(private httpClient: HttpClient) { }

  public get(apiURL){
    
    return this.httpClient.get(this.BASE_URI + apiURL);
  }

  public put(apiURL: String, data:any){

    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    return this.httpClient.put(this.BASE_URI + apiURL, data, httpOptions);
  }

}
