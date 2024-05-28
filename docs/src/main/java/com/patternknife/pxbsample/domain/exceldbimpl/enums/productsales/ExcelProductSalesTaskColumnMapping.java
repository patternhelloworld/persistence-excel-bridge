package com.patternknife.pxbsample.domain.exceldbimpl.enums.productsales;

public enum ExcelProductSalesTaskColumnMapping {
    TEMP1(0, "temp1", "temp1", "temp1"),
    ORDERACCOUNTR(1, "order_account_r", "OrderAccount__r", "orderAccountR"),
    ORDERACCOUNTRSKYZCODEC(2, "clinic_code", "OrderAccount__r.SkyZCode__c", "clinicCode"),
    ORDERACCOUNTRACCOUNTEXTERNALIDC(3, "order_account_r_account_external_id_c", "OrderAccount__r.AccountExternalID__c", "orderAccountRAccountExternalIDC"),
    ORDERACCOUNTRNAME(4, "clinic_name", "OrderAccount__r.Name", "clinicName"),
    ORDERACCOUNTRTYPE(5, "order_account_r_type", "OrderAccount__r.Type", "orderAccountRType"),
    ORDERACCOUNTRSUBTYPEC(6, "order_account_r_subtype_c","OrderAccount__r.SubType__c", "orderAccountRSubtypeC"),
    ORDERACCOUNTRPARENT(7, "order_account_r_parent", "OrderAccount__r.Parent", "orderAccountRParent"),
    ORDERACCOUNTRRATING(8, "order_account_r_rating", "OrderAccount__r.Rating", "orderAccountRRating"),
    ORDERACCOUNTRID(9, "order_account_r_id","OrderAccount__r.Id", "orderAccountRId"),
    ORDERACCOUNTROWNER(10, "order_account_r_owner", "OrderAccount__r.Owner", "orderAccountROwner"),
    ORDERACCOUNTROWNERDEPARTMENT(11, "order_account_r_owner_department", "OrderAccount__r.Owner.Department", "orderAccountROwnerDepartment"),
    ORDERACCOUNTRBILLINGCITY(12, "order_account_r_billing_city","OrderAccount__r.BillingCity", "orderAccountRBillingCity"),
    ORDERACCOUNTRBILLINGSTATE(13,"order_account_r.billing_state","OrderAccount__r.BillingState", "orderAccountRBillingState"),
    ORDERACCOUNTRBILLINGSTREET(14,"order_account_r_billing_street","OrderAccount__r.BillingStreet", "orderAccountRBillingStreet"),
    ORDERACCOUNTRBILLINGSTREET1C(15,"order_account_r_billing_street_1_c","OrderAccount__r.Billingstreet1__c", "orderAccountRBillingStreet1C"),
    ORDERID(16,"order_id","Id", "orderId"),
    NAME(17,"name","Name", "name"),
    ORDERDATEC(18,"order_date_c","OrderDate__c", "orderDateC"),
    ORDERTYPEC(19, "order_type_c","OrderType__c", "orderTypeC"),
    ORDERTYPEFORMULAC(20, "order_type_formula_c","OrderTypeFormula__c", "orderTypeFormulaC"),
    ORDERPRODUCTR(21, "order_product_r","OrderProduct__r", "orderProductR"),
    ORDERPRODUCTRPRODUCTSEGC(22, "order_product_r_product_seg_c", "OrderProduct__r.PRODUCT_SEG__c", "orderProductRProductSegC"),
    ORDERPRODUCTRPRODUCTSEG2C(23, "order_product_r_product_seg2_c", "OrderProduct__r.PRODUCT_SEG2__c", "orderProductRProductSeg2C"),
    ORDERPRODUCTRBELOTEROTYPEC(24, "order_product_r_belotero_type_c", "OrderProduct__r.Belotero_type__c", "orderProductRBeloteroTypeC"),
    ORDERPRODUCTRBELOTEROTYPE2C(25,"order_product_r.belotero_type2_c", "OrderProduct__r.Belotero_type2__c", "orderProductRBeloteroType2C"),
    ORDERPRODUCTRNAME(26, "order_product_r_name", "OrderProduct__r.Name", "orderProductRName"),
    ORDERAMOUNTC(27,"order_amount_c", "OrderAmount__c", "orderAmountC"),
    ORDERQUANTITYC(28, "order_quantity_c", "OrderQuantity__c", "orderQuantityC"),
    SALESREPR(29,"sales_rep_r", "SalesRep__r", "salesRepR"),
    SALESREPRNAME(30,"sales_rep_r_name", "SalesRep__r.Name", "salesRepRName"),
    ORDERACCOUNTRPARENTNAME(31,"order_account_r_parent_name", "OrderAccount__r.Parent.Name", "orderAccountRParentName");

    private final int columnNumber;
    private final String productSalesTableColumnName;
    private final String excelColumnName;
    private final String entityColumnName;

    ExcelProductSalesTaskColumnMapping(int columnNumber, String productSalesTableColumnName, String excelColumnName, String entityColumnName) {
        this.columnNumber = columnNumber;
        this.productSalesTableColumnName = productSalesTableColumnName;
        this.excelColumnName = excelColumnName;
        this.entityColumnName = entityColumnName;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public String getProductSalesTableColumnName() {
        return productSalesTableColumnName;
    }

    public String getExcelColumnName() {
        return excelColumnName;
    }

    public String getEntityColumnName() {
        return entityColumnName;
    }
}
