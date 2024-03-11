/*
 * Author: Dillon Steve Juriansz
 * IIT ID: 20221517
 * UoW ID: w1958027
 * Description: This Shopping Car GUI class which is for showing the user;s shopping cart and prices.
 * Date: 12/01/2024
 * */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart extends JFrame {
    private boolean firstTime;
    private Map<String, Integer> productDetails = new HashMap<>();
    private ArrayList<Product> cart = new ArrayList<>();
    private GridBagConstraints gridBagConstraints;
    private JPanel mainPanel, pricesPanel;
    private DefaultTableModel defaultTableModel;
    private JTable table;
    private JScrollPane tableScroll;
    private DefaultTableCellRenderer tableCellRenderer;
    private JLabel total, totalValue, initialPurchaseDiscount, getInitialPurchaseDiscountValue,
            bulkPurchaseDiscount, getBulkPurchaseDiscountValue, finalTotal, finalTotalValue;

    public ShoppingCart(){}

    public ShoppingCart(boolean firstTime){
        this.firstTime = firstTime;
    }

    public void build(){
        setTitle("Shopping Cart");
        setSize(600, 600);
        setLayout(new GridBagLayout());

        mainPanel = new JPanel(new GridBagLayout());
        pricesPanel = new JPanel(new GridBagLayout());

        gridBagConstraints = new GridBagConstraints();

        buildTable();

        detailsBuild();


        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(mainPanel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(pricesPanel, gridBagConstraints);
    }

    public void setVisible() {
        setVisible(true);
    }

    private void buildTable() {
        defaultTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        defaultTableModel.addColumn("Product");
        defaultTableModel.addColumn("Quantity");
        defaultTableModel.addColumn("Price £");

        table = new JTable(defaultTableModel);


        table.setRowHeight(50);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(tableHeader.getFont().deriveFont(Font.BOLD));
        tableHeader.setReorderingAllowed(false);
        tableCellRenderer = new DefaultTableCellRenderer();




        tableScroll = new JScrollPane(table);

        table.setPreferredScrollableViewportSize(new Dimension(450, 250));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(tableScroll, gridBagConstraints);

        tableScroll.revalidate();
        tableScroll.repaint();
        revalidate();
        repaint();

    }

    private void detailsBuild(){
        total = new JLabel("Total ");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        pricesPanel.add(total, gridBagConstraints);

        initialPurchaseDiscount = new JLabel("First Purchase Discount (10%) ", SwingConstants.RIGHT);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        pricesPanel.add(initialPurchaseDiscount, gridBagConstraints);

        bulkPurchaseDiscount = new JLabel("Three Item in Same Category Discount (20%) ", SwingConstants.RIGHT);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        pricesPanel.add(bulkPurchaseDiscount, gridBagConstraints);

        finalTotal = new JLabel("Final Total ", SwingConstants.RIGHT);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        pricesPanel.add(finalTotal, gridBagConstraints);

        totalValue = new JLabel();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        pricesPanel.add(totalValue, gridBagConstraints);


        getInitialPurchaseDiscountValue = new JLabel();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        pricesPanel.add(getInitialPurchaseDiscountValue, gridBagConstraints);

        getBulkPurchaseDiscountValue = new JLabel();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        pricesPanel.add(getBulkPurchaseDiscountValue, gridBagConstraints);


        finalTotalValue = new JLabel();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        pricesPanel.add(finalTotalValue, gridBagConstraints);

    }


    public void addToCart(Product product) {
        if (productDetails.containsKey(product.getProductId())) {
            int quantity = productDetails.get(product.getProductId());
            productDetails.put(product.getProductId(), quantity + 1);
        } else {
            cart.add(product);
            productDetails.put(product.getProductId(), 1);
        }
        updateShoppingTable();
    }

    /**
     * Updates the shopping cart table with the current items and quantities in the cart.
     * Also updates the price in the UI accordingly.
     */
    private void updateShoppingTable() {
        defaultTableModel.setRowCount(0);


        String formattedPrice;

        for (Product product : cart) {
            String id = product.getProductId();
            String name = product.getProductName();
            String information1;
            String information2;
            int quantity = productDetails.get(product.getProductId());
            double price = product.getPrice() * quantity;
            formattedPrice = String.format("%.2f", price);
            if (product instanceof Electronics) {
                information1 = ((Electronics) product).getBrand();
                information2 = "" + ((Electronics) product).getWarrantyPeriod();
                defaultTableModel.addRow(new Object[]{"<html><center>" + id + "<br>" + name + "</br><br>" + information1 + "</br>, " + information2 + "w/w" + "</centre></html.", quantity, formattedPrice});
            } else if (product instanceof Clothing) {
                information1 = ((Clothing) product).getSize();
                information2 = ((Clothing) product).getColor();
                defaultTableModel.addRow(new Object[]{"<html><center>" + id + "<br>" + name + "</br><br>" + information1 + "</br>, " + information2 + "</centre></html.", quantity, formattedPrice});
            }
        }


        tableCellRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++){
            table.getColumnModel().getColumn(i).setCellRenderer(tableCellRenderer);
        }

        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        tableScroll.revalidate();
        tableScroll.repaint();
        calculateCartTotal();
    }

    public void removeFromCart() {
    }

    /**
     * Calculates the total price of items in the shopping cart considering discounts.
     * Updates the UI components to display the calculated values.
     */
    public void calculateCartTotal() {
        double totalPrice = 0;
        int electronic = 0;
        int clothing = 0;
        double sameCategoryDiscount = 0.0;
        double firstTimeDiscount = 0.0;
        double finalTotal;

        for (Product item : cart){
            totalPrice += productDetails.get(item.getProductId()) * item.getPrice();
            if (item instanceof Electronics){
                electronic++;
            } else if (item instanceof Clothing){
                clothing++;
            }
        }
        if (electronic >= 3 || clothing >= 3){
            sameCategoryDiscount = totalPrice * 0.20;
        }
        if (firstTime){
            firstTimeDiscount = totalPrice * 0.10;
        }
        String formattedPrice;
        formattedPrice = String.format("%.2f", totalPrice);
        totalValue.setText(formattedPrice + "£");

        formattedPrice = String.format("%.2f", firstTimeDiscount);
        getInitialPurchaseDiscountValue.setText("-" +formattedPrice + "£");

        formattedPrice = String.format("%.2f", sameCategoryDiscount);
        getBulkPurchaseDiscountValue.setText("-" + formattedPrice + "£");

        finalTotal = totalPrice - (firstTimeDiscount + sameCategoryDiscount);
        formattedPrice = String.format("%.2f", finalTotal);

        finalTotalValue.setText(formattedPrice + "£");
    }
}