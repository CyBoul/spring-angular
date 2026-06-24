import { ErrorHandler, Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  handleError(error: unknown): void {
    if (error instanceof HttpErrorResponse) {
      console.error(`[HTTP ${error.status}] ${error.url}: ${error.message}`);
    } else if (error instanceof Error) {
      console.error(`[App Error] ${error.message}`, error.stack);
    } else {
      console.error('[Unknown Error]', error);
    }
  }
}
