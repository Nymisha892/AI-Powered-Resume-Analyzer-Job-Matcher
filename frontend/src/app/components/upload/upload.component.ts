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

  constructor(private uploadService: UploadService, private snackBar: MatSnackBar) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedFile = input.files?.[0] || null;
  }

  upload(): void {
    if (!this.selectedFile) {
      this.snackBar.open('Please select a PDF file', 'Close', { duration: 3000 });
      return;
    }

    this.loading = true;
    this.uploadService.uploadResume(this.selectedFile).subscribe({
      next: (res) => {
        this.loading = false;
        this.snackBar.open('Resume uploaded successfully!', 'Close', { duration: 3000 });
      },
      error: (err) => {
        this.loading = false;
        this.snackBar.open('Upload failed. Try again.', 'Close', { duration: 3000 });
        console.error(err);
      }
    });
  }
}
