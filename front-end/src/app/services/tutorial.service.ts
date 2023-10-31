import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Tutorial} from "../models/tutorial.model";

const baseUrl = 'http://localhost:8080/api/tutorials';

@Injectable({
  providedIn: 'root'
})
export class TutorialService {

  constructor(private http: HttpClient) { }

  getAll(params: any): Observable<any>{
    return this.http.get<Tutorial[]>(baseUrl, { params });
  }

  getTutorial(id: any): Observable<Tutorial>{
    return this.http.get(`${baseUrl}/${id}`);
  }

  create(tutorial: any): Observable<any>{
    return this.http.post(baseUrl, tutorial);
  }

  update(id:number, tutorial:any): Observable<any>{
    return this.http.put(`${baseUrl}/${id}`, tutorial);
  }

  delete(id:number): Observable<any>{
    return this.http.delete(`${baseUrl}/${id}`, {responseType: 'text'});
  }

  deleteAll(): Observable<any>{
    return this.http.delete(baseUrl);
  }

  findByTitle(title: string): Observable<Tutorial[]>{
    console.log(`${baseUrl}?title=${title}`);
    return this.http.get<Tutorial[]>(`${baseUrl}?title=${title}`);
  }

  findPublished(): Observable<Tutorial[]>{
    return this.http.get<Tutorial[]>(baseUrl + '/published');
  }
}
