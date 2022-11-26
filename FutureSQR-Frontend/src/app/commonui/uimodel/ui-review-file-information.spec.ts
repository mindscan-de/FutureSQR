import { UiReviewFileInformation } from './ui-review-file-information';

describe('UiReviewFileInformation', () => {
  it('should create an instance', () => {
    expect(new UiReviewFileInformation("/filepath","ACTION",true)).toBeTruthy();
  });
});
