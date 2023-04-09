export default class Component {
    constructor(data) {
      this.id = data?.id;
      this.price = data?.price || "";
      this.componentName = data?.componentName || "";
    }
  }