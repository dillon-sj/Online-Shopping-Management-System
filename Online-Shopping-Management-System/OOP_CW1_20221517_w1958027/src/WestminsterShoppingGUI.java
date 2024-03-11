/*
 * Author: Dillon Steve Juriansz
 * IIT ID: 20221517
 * UoW ID: w1958027
 * Description: This Shopping Center GUI class which is for the shopping center interface for the user
 * Date: 12/01/2024
 * */


import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;



public class WestminsterShoppingGUI extends JFrame {
    private JLabel selectProductDetails, productIdLabel, productCategory, productName,  productInfo1, productInfo2, productAvailability,category;
    private JPanel main, productDetails;

    private JComboBox<String> dropdown;
    private GridBagConstraints gridBagConstraints;
    private ArrayList<Product> products;
    private DefaultTableModel defaultTableModel;
    private JTable table;

    private JScrollPane scrollPaneProductTable;

    private WestminsterShoppingManager shoppingManager;
    private ShoppingCart shoppingCart;

    private boolean firstTimeLogin;


    /**
     * Constructs a WestminsterShoppingGUI object.
     *
     * @param products      The list of products.
     * @param firstTimeLogin A flag indicating whether it's the user's first time login.
     */
    public WestminsterShoppingGUI(ArrayList<Product> products,boolean firstTimeLogin) {
        shoppingManager = new WestminsterShoppingManager();
        this.products = new ArrayList<>(products);
        this.firstTimeLogin = firstTimeLogin;
        ShoppingCart firstTimeHere = new ShoppingCart(firstTimeLogin);
        createUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Call the save method when the window is closing
                shoppingManager.saveProductToFile(products);
                System.out.println("\t\tWindow is closing\n");
                System.exit(0);
            }
        });
    }





    private void createUI() {
        setSize(900, 700);
        setTitle("Westminster Shopping Center");

        setLayout(new GridBagLayout());

        shoppingCart = new ShoppingCart(firstTimeLogin);
        shoppingCart.build();

        main = new JPanel(new GridBagLayout());
        productDetails = new JPanel(new GridLayout(7, 1,10,10));
        gridBagConstraints = new GridBagConstraints();



        setupLabels();
        addProductCategoryLabel();
        selectDropdown();
        addShoppingCartButton();
        createAndConfigureTable();
        populateTable("All");
        separator();
        details();
        addAddToShoppingCartButton();

        add(main);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        revalidate();
        repaint();
        System.out.println("\tShopping Center GUI created");
    }


    /**
     * Sets up label fonts and initializes labels used in the UI.
     */
    private void setupLabels() {
        selectProductDetails = createLabel("Select Product Details");
        productIdLabel = createLabel("Selected Product ID: ");
        productCategory = createLabel("Category: ");
        productName = createLabel("Name: ");
        productInfo1 = createLabel("Info 1: ");
        productInfo2 = createLabel("Info 2: ");
        productAvailability = createLabel("Items Available: ");

        Font regularFont = UIManager.getFont("Label.font").deriveFont(Font.PLAIN);
        productIdLabel.setFont(regularFont);
        productCategory.setFont(regularFont);
        productName.setFont(regularFont);
        productInfo1.setFont(regularFont);
        productInfo2.setFont(regularFont);
        productAvailability.setFont(regularFont);
    }

    /**
     * Creates a JLabel with the specified text.
     *
     * @param text The text for the label.
     * @return A new JLabel with the specified text.
     */
    private JLabel createLabel(String text) {
        return new JLabel(text);
    }



    /**
     * Adds the product category label to the main panel.
     */
    private void addProductCategoryLabel() {
        category = createLabel("Select Product Category");
//
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 220, 0, 0);
        main.add(category, gridBagConstraints);
    }


    /**
     * Creates and adds a dropdown menu for selecting product categories to the main panel.
     */
    private void selectDropdown() {
        String[] items = {"All", "Electronics", "Clothing"};
        dropdown = new JComboBox<>(items);
        dropdown.addActionListener(e -> updateTable((String) dropdown.getSelectedItem()));
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0,10,0,0);
        main.add(dropdown, gridBagConstraints);

    }

    /**
     * Adds the "Shopping Cart" button to the main panel.
     */
    private void addShoppingCartButton() {
        JButton shoppingCartBtn = new JButton("Shopping Cart");

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new Insets(0,0,100,0);
        main.add(shoppingCartBtn, gridBagConstraints);

        //setting the weighty value to default
        gridBagConstraints.weighty = 0;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        shoppingCartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shoppingCart.setVisible();
            }
        });

    }

    /**
     * Creates and configures the product table, including setting up the model and rendering properties.
     */
    private void createAndConfigureTable() {
        defaultTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        defaultTableModel.addColumn("Product ID");
        defaultTableModel.addColumn("Name");
        defaultTableModel.addColumn("Category");
        defaultTableModel.addColumn("Price");
        defaultTableModel.addColumn("Info");

        table = new JTable(defaultTableModel);
        table.setRowHeight(30);

        // Set center alignment for all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                Object[] rowData = new Object[table.getColumnCount()];
                for (int i = 0; i < table.getColumnCount(); i++) {
                    rowData[i] = table.getValueAt(selectedRow, i);
                }
                updateDetailsPanel(rowData);
            }
        });



        JTableHeader headers = table.getTableHeader();
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) headers.getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        headers.setFont(headers.getFont().deriveFont(Font.BOLD));
        headers.setReorderingAllowed(false);

        scrollPaneProductTable = new JScrollPane(table);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints.fill = GridBagConstraints.CENTER;
        scrollPaneProductTable.setPreferredSize(new Dimension(800, 180));
        main.add(scrollPaneProductTable, gridBagConstraints);


    }

    /**
     * Populates the table with product information based on the selected product category.
     * If a specific category is chosen, only products in that category will be displayed.
     * If "All" is selected, products from all categories will be shown.
     *
     * @param checkCategoryId (String) the selected product category to filter the products.
     */
    private void populateTable(String checkCategoryId) {
        defaultTableModel.setRowCount(0);
        for (Product product : products) {
            String productCategory = product.getProductCategory();
            if (!"All".equals(checkCategoryId) && !productCategory.equals(checkCategoryId)) {
                continue;
            }
            String name = product.getProductName();
            String price = String.valueOf(product.getPrice());
            String productInfo = getProductInfo(product);

            Object[] rowData = {product.getProductId(), name, productCategory, price, productInfo};
            defaultTableModel.addRow(rowData);
        }
    }
    private void details() {
        productDetails.add(selectProductDetails);
        productDetails.add(productIdLabel);
        productDetails.add(productCategory);
        productDetails.add(productName);
        productDetails.add(productInfo1);
        productDetails.add(productInfo2);
        productDetails.add(productAvailability);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.CENTER;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new Insets(0,0,0,0);
        main.add(productDetails, gridBagConstraints);
    }

    /**
     * Updates the details panel with information corresponding to the selected product.
     *
     * @param rowData (Object[]) an array containing information about the selected product.
     */
    private void updateDetailsPanel(Object[] rowData) {
        for (Product product : products) {
            if (product.getProductId().equals(rowData[0])) {
                productIdLabel.setText("Selected Product ID: " + rowData[0]);
                productName.setText("Name: " + rowData[1]);
                productCategory.setText("Category: " + rowData[2]);
                productAvailability.setText("Availability: " + product.getAvailableItems());

                int availability = product.getAvailableItems();
                if (availability > 3) {
                    setRowColor(table, table.getSelectedRow(), Color.BLUE);
                } else if (availability >= 1 && availability <= 3) {
                    setRowColor(table, table.getSelectedRow(), Color.RED);
                } else {
                    // Reset background color
                    setRowColor(table, table.getSelectedRow(), null);
                }


                if (product instanceof Electronics) {
                    productInfo1.setText("Brand Name: " + ((Electronics) product).getBrand());
                    productInfo2.setText("Warranty: " + ((Electronics) product).getWarrantyPeriod());
                } else {
                    productInfo1.setText("Size: " + ((Clothing) product).getSize());
                    productInfo2.setText("Colour: " + ((Clothing) product).getColor());

                }
            }
        }

    }

    /**
     * Sets the background color for the selected row in the table.
     * @param table The table to set the row color.
     * @param row   The index of the row to set the color.
     * @param color The color to set for the row.
     */
    private void setRowColor(JTable table, int row, Color color) {
        table.setSelectionBackground(color);
        table.setSelectionForeground(Color.WHITE);
    }

    /**
     * Retrieves additional information about a product based on its category.
     * @param product The product for which to retrieve additional information.
     * @return A string containing additional information about the product.
     */
    private String getProductInfo(Product product) {
        if ("Electronics".equals(product.getProductCategory())) {
            Electronics electronics = (Electronics) product;
            return electronics.getBrand() + ", " + electronics.getWarrantyPeriod() + " weeks of warranty";
        } else if ("Clothing".equals(product.getProductCategory())) {
            Clothing clothing = (Clothing) product;
            return clothing.getSize()  + ", " + clothing.getColor();
        } else {
            return "Error in Loading product info";
        }
    }

    /**
     * Updates the product table based on the selected category.
     * @param category The selected product category.
     */
    private void updateTable(String category) {
        populateTable(category);

    }

    //separator

    private void separator(){
        JSeparator separatorLine = new JSeparator();
        gridBagConstraints.insets = new Insets(15, 0, 15, 0);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.weightx = 1;
        main.add(separatorLine, gridBagConstraints);

        //reset to default
        gridBagConstraints.fill = GridBagConstraints.NONE;
    }


    private void addAddToShoppingCartButton() {
        JButton addToShoppingCart = new JButton("Add to Shopping Cart");
        addToShoppingCart.addActionListener(e -> addToShoppingCartButtonClicked());
        JPanel button = new JPanel(new BorderLayout());
        button.add(addToShoppingCart,BorderLayout.CENTER);
        button.setBackground(Color.RED);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        main.add(button, gridBagConstraints);
    }

    /**
     * Handles the event when the "Add to Shopping Cart" button is clicked.
     * It adds the selected product to the shopping cart, updates availability, and refreshes the UI.
     */
    private void addToShoppingCartButtonClicked() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No item selected. Please select an item to add to the shopping cart.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
        } else {
            String productId = (String) table.getValueAt(selectedRow, 0);
            Product selectedProduct = findProductById(productId);
            if (selectedProduct != null && selectedProduct.getAvailableItems() > 0) {
                selectedProduct.setAvailableItems(selectedProduct.getAvailableItems() - 1);
                shoppingCart.addToCart(selectedProduct);
                JOptionPane.showMessageDialog(this, "Item added to the shopping cart.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Update the products table UI
                populateTable((String) dropdown.getSelectedItem());
                updateDetailsPanel(new Object[]{selectedProduct.getProductId(), selectedProduct.getProductName(), selectedProduct.getProductCategory(), selectedProduct.getPrice(), getProductInfo(selectedProduct)});

            } else {
                JOptionPane.showMessageDialog(this, "Selected item is not available.", "Not Available", JOptionPane.WARNING_MESSAGE);
            }

            }
        }

    /**
     * Finds a product in the list based on its ID.
     *
     * @param productId The ID of the product to find.
     * @return The found product or null if not found.
     */
    private Product findProductById(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
}
