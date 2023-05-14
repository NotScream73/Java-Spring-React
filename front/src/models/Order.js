export default class Order {
    constructor(data) {
        this.id = data?.id;
        this.date = data?.date || "";
        this.price = data?.price || 0;
        this.productDTOList = data?.productDTOList || [];
        this.status = data?.status || "0";
        this.user = data?.user || "";
    }
}