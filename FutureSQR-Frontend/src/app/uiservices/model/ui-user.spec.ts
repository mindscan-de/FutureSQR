import { UiUser } from './ui-user';

describe('UiUser', () => {
  it('should create an instance', () => {
    expect(new UiUser("uuid","DisplayName", "/assets/Location.png")).toBeTruthy();
  });
});
