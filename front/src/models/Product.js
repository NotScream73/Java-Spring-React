export default class Product {
  constructor(data) {
    this.id = data?.id;
    this.price = data?.price || "";
    this.name = data?.name || "";
    this.count = data?.count || 0;
    this.image = data?.image || "";
    this.componentDTOList = data?.componentDTOList || [];
  }
}