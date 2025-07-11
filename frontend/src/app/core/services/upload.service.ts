import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UploadService {
  private baseUrl = 'http://localhost:8080/api/resume'; // Spring Boot endpoint

  constructor(private http: HttpClient) {}

  uploadResume(formData: FormData): Observable<any> {
    // const formData = new FormData();
    // formData.append('file', file);
    // formData.append('userid',"1");
    return this.http.post(`${this.baseUrl}/upload`, formData);
  }

  getMatchingJobs(resumeId: string): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/match-jobs/${resumeId}`);
}


}
