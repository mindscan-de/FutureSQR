import { NavbarBreadcrumbItem } from './navbar-breadcrumb-item';

describe('NavbarBreadcrumbItem', () => {
  it('should create an instance', () => {
    expect(new NavbarBreadcrumbItem("linktext",[],false)).toBeTruthy();
  });
});
