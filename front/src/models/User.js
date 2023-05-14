export default class User {
    constructor(data) {
      this.id = data?.id;
      this.login = data?.login || "";
      this.role = data?.role || "";
    }
  }