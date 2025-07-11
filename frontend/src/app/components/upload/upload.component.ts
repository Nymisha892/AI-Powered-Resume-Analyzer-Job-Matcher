import { Component } from '@angular/core';
import { UploadService } from 'src/app/core/services/upload.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent {
  selectedFile: File | null = null;
  loading = false;
  extractedSkills: any;
  suggestedRole: any;
  resumeId: any;
  matchedJobs: any[] | undefined;

  constructor(private uploadService: UploadService, private snackBar: MatSnackBar) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedFile = input.files?.[0] || null;
  }

  // upload(): void {
  //   if (!this.selectedFile) {
  //     this.snackBar.open('Please select a PDF file', 'Close', { duration: 3000 });
  //     return;
  //   }

  //   this.loading = true;
  //   this.uploadService.uploadResume(this.selectedFile).subscribe({
  //     next: (res) => {
  //       this.loading = false;
  //       this.snackBar.open('Resume uploaded successfully!', 'Close', { duration: 3000 });
  //     },
  //     error: (err) => {
  //       this.loading = false;
  //       this.snackBar.open('Upload failed. Try again.', 'Close', { duration: 3000 });
  //       console.error(err);
  //     }
  //   });
  // }

  upload(): void {
  if (!this.selectedFile) {
    this.snackBar.open('Please select a PDF file.', 'Close', { duration: 3000 });
    return;
  }

  const formData = new FormData();
  formData.append('file', this.selectedFile);
  formData.append('userid', '1'); // Replace with real userId or JWT-based later

  this.loading = true;
  

  this.uploadService.uploadResume(formData).subscribe({
    next: (res) => {
      this.loading = false;
      this.snackBar.open('Resume uploaded successfully!', 'Close', { duration: 3000 });
      console.log("Response",res);
      this.extractedSkills = res.extractedSkills;
      this.suggestedRole = res.suggestedRole;
      this.resumeId = res.resumeId;

      this.fetchMatchingJobs(this.resumeId);
    },
    error: () => {
      this.loading = false;
      console.log("response")
      this.snackBar.open('Upload failed. Try again.', 'Close', { duration: 3000 });
    }
  });
}
  fetchMatchingJobs(resumeId: string): void {
  this.uploadService.getMatchingJobs(resumeId).subscribe({
    next: (jobs) => {
      this.matchedJobs = jobs;
    },
    error: () => {
      this.snackBar.open('Failed to fetch matching jobs.', 'Close', { duration: 3000 });
    }
  });
}

}
