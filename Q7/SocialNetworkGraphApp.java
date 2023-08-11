
// Import necessary libraries
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

// The main application class
public class SocialNetworkGraphApp {

    // The main entry point of the application
    public static void main(String[] args) {
        // Start the application using Swing utilities
        SwingUtilities.invokeLater(() -> {
            // Create the main application window
            JFrame frame = new JFrame("Social Network Graph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create the panel that will display the social network graph
            SocialNetworkGraphPanel graphPanel = new SocialNetworkGraphPanel();
            frame.add(graphPanel, BorderLayout.CENTER);

            // Create the toolbar with buttons
            JToolBar toolBar = new JToolBar();
            JButton addNodeButton = new JButton("Add Node");
            JButton addEdgeButton = new JButton("Add Edge");

            // Add action listeners to the buttons
            addNodeButton.addActionListener(e -> graphPanel.addNewNode());
            addEdgeButton.addActionListener(e -> graphPanel.addNewEdge());

            // Add buttons to the toolbar
            toolBar.add(addNodeButton);
            toolBar.add(addEdgeButton);
            frame.add(toolBar, BorderLayout.NORTH);

            // Create a search field and search button
            JTextField searchField = new JTextField();
            searchField.setColumns(20);
            JButton searchButton = new JButton("Search");

            // Add action listener to the search button
            searchButton.addActionListener(e -> {
                // Get the search query from the search field
                String searchQuery = searchField.getText();
                // Search for and highlight nodes based on the query
                graphPanel.searchAndHighlightNode(searchQuery);
                // Repaint the graph panel to reflect the changes
                graphPanel.repaint();
            });

            // Create a panel to hold the search components
            JPanel searchPanel = new JPanel();
            searchPanel.add(new JLabel("Search User: "));
            searchPanel.add(searchField);
            searchPanel.add(searchButton);

            // Add the search panel to the main frame
            frame.add(searchPanel, BorderLayout.SOUTH);

            // Set the frame to full-screen and make it visible
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);

            // Set focus to the graph panel
            graphPanel.requestFocusInWindow();
        });
    }
}

// This class represents a custom JPanel for displaying a social network graph.

class SocialNetworkGraphPanel extends JPanel {

    // Directory where user data and connections are stored
    private static String directory = System.getProperty("user.dir") + "/Q7/";

    // Lists to store nodes and edges of the graph
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    // Map to quickly access nodes using their IDs
    private Map<String, Node> nodeMap = new HashMap<>();

    // Variables to keep track of user interactions
    private Node selectedNode = null;
    private Node selectedEdge = null;
    private Node draggingNode = null;
    private Point mouseOffset = new Point();

    // Constructor initializes the graph panel
    public SocialNetworkGraphPanel() {
        // Read user data and connections from files
        readUserDataFromFile(directory + "users.txt");
        readConnectionsFromFile(directory + "connection.txt");

        // Adjust positions of nodes based on their connections
        adjustNodePositions();

        // Add a mouse listener to handle node selection and highlighting
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Deselect all nodes and highlight the clicked node
                deSelectAllNodes();
                highlightSelectedNode();
                repaint(); // Redraw the panel
            }
        });

        // Add a key listener to handle node and edge deletion
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    // Remove selected node and edge if applicable
                    removeSelectedNode();
                    removeSelectedEdge();
                    repaint(); // Update the panel
                }
            }
        });

        // Add a mouse listener to handle edge selection and highlighting
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Deselect all edges and highlight the clicked edge
                deSelectAllEdges();
                highlightSelectedEdge();
                repaint(); // Refresh the panel
            }
        });

        // Add a mouse listener to handle node dragging
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Determine if a node is being dragged and set initial offset
                draggingNode = getSelectedNode(e.getPoint());
                if (draggingNode != null) {
                    mouseOffset.setLocation(e.getPoint().getX() - draggingNode.x, e.getPoint().getY() - draggingNode.y);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggingNode = null; // Clear dragging state
            }
        });

        // Add a mouse motion listener to handle node dragging movement
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingNode != null) {
                    // Update the position of the dragging node
                    draggingNode.x = (int) (e.getPoint().getX() - mouseOffset.getX());
                    draggingNode.y = (int) (e.getPoint().getY() - mouseOffset.getY());
                    repaint(); // Update the panel
                }
            }
        });

        // Set the panel as focusable and request focus
        setFocusable(true);
        requestFocus();
    }

    // Search for a node based on a search query and highlight it if found
    public void searchAndHighlightNode(String searchQuery) {
        // Deselect all currently selected nodes
        deSelectAllNodes();

        // Check if the search query is provided and not empty
        if (searchQuery != null && !searchQuery.isEmpty()) {
            // Retrieve the node from the nodeMap based on the search query
            Node matchingNode = nodeMap.get(searchQuery);

            // If a matching node is found, mark it as selected
            if (matchingNode != null) {
                matchingNode.isSelected = true;
            }
        }

        // Repaint the graphical representation
        repaint();
    }

    // Add a new node to the graph
    public void addNewNode() {
        // Prompt user for a username
        String userName = JOptionPane.showInputDialog(this, "Enter Username:");
        if (userName != null && !userName.isEmpty()) {
            // Prompt user for the number of followers
            String followersStr = JOptionPane.showInputDialog(this, "Enter Followers:");
            if (followersStr != null && !followersStr.isEmpty()) {
                try {
                    // Parse the follower count as an integer
                    int followers = Integer.parseInt(followersStr);

                    // Create a new node with the provided information and position
                    Node newNode = new Node(200, 200, userName, followers);

                    // Open a file chooser dialog to select a profile image
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(this);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        newNode.profileImagePath = selectedFile.getAbsolutePath();
                    }

                    // Add the new node to the nodes list and nodeMap
                    nodes.add(newNode);
                    nodeMap.put(userName, newNode);

                    // Adjust the positions of nodes on the graph and repaint
                    adjustNodePositions();
                    repaint();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid number format for followers.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Add a new edge connecting two nodes
    public void addNewEdge() {
        // Prompt user for the usernames of two nodes to connect
        String user1Name = JOptionPane.showInputDialog(this, "Enter Username for User 1:");
        if (user1Name != null && !user1Name.isEmpty()) {
            String user2Name = JOptionPane.showInputDialog(this, "Enter Username for User 2:");
            if (user2Name != null && !user2Name.isEmpty()) {
                // Prompt user for the strength of the connection
                String strengthStr = JOptionPane.showInputDialog(this, "Enter Strength:");
                if (strengthStr != null && !strengthStr.isEmpty()) {
                    try {
                        // Parse the strength as an integer
                        int strength = Integer.parseInt(strengthStr);

                        // Retrieve nodes based on provided usernames
                        Node node1 = nodeMap.get(user1Name);
                        Node node2 = nodeMap.get(user2Name);

                        // If both nodes exist, create an edge between them and repaint
                        if (node1 != null && node2 != null) {
                            edges.add(new Edge(node1, node2, "Strength: " + strength));
                            repaint();
                        } else {
                            JOptionPane.showMessageDialog(this, "One or both user nodes not found.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid number format for strength.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    // This method reads user data from a file and populates the data into Node
    // objects.
    private void readUserDataFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into parts based on spaces.
                String[] parts = line.split(" ");

                // Check if there are exactly 4 parts in the line.
                if (parts.length == 4) {
                    // Extract user data from the parts.
                    String userName = parts[0];
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    int followers = Integer.parseInt(parts[3]);

                    // Create a new Node object using the extracted data.
                    Node node = new Node(x, y, userName, followers);

                    // Set the profile image path for the user.
                    node.profileImagePath = System.getProperty("user.dir") + "/Q7/images/" + userName + ".jpg";

                    // Add the Node object to a list and map for future reference.
                    nodes.add(node);
                    nodeMap.put(userName, node);
                }
            }
        } catch (IOException e) {
            // Print the stack trace if an IOException occurs.
            e.printStackTrace();
        }
    }

    // This method reads connection data from a file and creates edges between
    // Nodes.
    private void readConnectionsFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into parts based on spaces.
                String[] parts = line.split(" ");

                // Check if there are exactly 3 parts in the line.
                if (parts.length == 3) {
                    // Extract connection data from the parts.
                    String user1 = parts[0];
                    String user2 = parts[1];
                    int strength = Integer.parseInt(parts[2]);

                    // Retrieve the Node objects corresponding to the users.
                    Node node1 = nodeMap.get(user1);
                    Node node2 = nodeMap.get(user2);

                    // If both Node objects are found, create an Edge between them.
                    if (node1 != null && node2 != null) {
                        edges.add(new Edge(node1, node2, "Strength: " + strength));
                    }
                }
            }
        } catch (IOException e) {
            // Print the stack trace if an IOException occurs.
            e.printStackTrace();
        }
    }

    // This method adjusts the positions of nodes to prevent overlapping and loads
    // profile images for each node.
    private void adjustNodePositions() {
        int spacing = 150; // Define the spacing between nodes
        Set<Point> usedPositions = new HashSet<>(); // Set to keep track of used positions

        // Loop through each node
        for (Node node : nodes) {
            Point position = new Point(node.x, node.y); // Get the current position of the node

            // Keep adjusting the position until a non-overlapping position is found
            while (usedPositions.contains(position)) {
                position.translate(spacing, 0); // Move the position to the right by 'spacing' units
            }

            // Update the node's position and mark the position as used
            node.x = position.x;
            node.y = position.y;
            usedPositions.add(position);

            // Load and set the profile image for the node
            try {
                BufferedImage image = ImageIO.read(new File(node.profileImagePath));
                if (image != null) {
                    node.profileImage = image;
                }
            } catch (IOException e) {
                e.printStackTrace(); // Print error if image loading fails
            }
        }
    }

    // This method highlights the edge that the mouse cursor is currently over.
    private void highlightSelectedEdge() {
        Point mousePosition = getMousePosition(); // Get the current mouse cursor position
        if (mousePosition != null) {
            Edge selectedEdge = getSelectedEdge(mousePosition); // Find the edge under the cursor
            if (selectedEdge != null) {
                selectedEdge.isSelected = true; // Mark the edge as selected
            }
        }
    }

    // This method deselects all edges, removing their selected state.
    private void deSelectAllEdges() {
        for (Edge edge : edges) {
            edge.isSelected = false; // Set the selected state of all edges to false
        }
    }

    // This method removes a selected node and its connected edges from the graph.
    private void removeSelectedNode() {
        Point mousePosition = getMousePosition(); // Get the current mouse cursor position
        if (mousePosition != null) {
            Node selectedNode = getSelectedNode(mousePosition); // Find the node under the cursor
            if (selectedNode != null) {
                nodes.remove(selectedNode); // Remove the selected node from the list of nodes

                // Also remove any edges connected to the selected node
                edges.removeIf(edge -> edge.startNode == selectedNode || edge.endNode == selectedNode);
            }
        }
    }

    // This method removes a selected edge from the graph.
    private void removeSelectedEdge() {
        Point mousePosition = getMousePosition(); // Get the current mouse cursor position
        if (mousePosition != null) {
            Edge selectedEdge = getSelectedEdge(mousePosition); // Find the edge under the cursor
            if (selectedEdge != null) {
                edges.remove(selectedEdge); // Remove the selected edge from the list of edges
            }
        }
    }

    // This method highlights the node under the mouse cursor, if any.
    private void highlightSelectedNode() {
        // Get the current mouse position
        Point mousePosition = getMousePosition();

        // Check if the mouse position is not null
        if (mousePosition != null) {
            // Get the node at the mouse position
            Node selectedNode = getSelectedNode(mousePosition);

            // If a node is found, mark it as selected
            if (selectedNode != null) {
                selectedNode.isSelected = true;
            }
        }
    }

    // This method deselects all nodes by setting their 'isSelected' property to
    // false.
    private void deSelectAllNodes() {
        for (Node node : nodes) {
            node.isSelected = false;
        }
    }

    // This method returns the node that contains the given point.
    private Node getSelectedNode(Point point) {
        for (Node node : nodes) {
            if (node.contains(point)) {
                return node;
            }
        }
        return null; // No node found at the given point
    }

    // This method returns the edge that contains the given point.
    private Edge getSelectedEdge(Point point) {
        for (Edge edge : edges) {
            if (edge.contains(point)) {
                return edge;
            }
        }
        return null; // No edge found at the given point
    }

    // Size of the grid for drawing nodes and edges
    private final int GRID_SIZE = 20;

    // This method is called when the component needs to be painted.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the grid on the component
        drawGrid(g);

        // Draw all the edges
        for (Edge edge : edges) {
            edge.draw(g);
        }

        // Draw all the nodes
        for (Node node : nodes) {
            node.draw(g);
        }
    }

    // Private class representing a node in the graph
    private class Node {
        public String profileImagePath;
        private BufferedImage profileImage;
        int x, y;
        String userName;
        int followers;

        boolean isSelected = false;

        // Constructor for creating a node
        Node(int x, int y, String userName, int followers) {
            this.x = x;
            this.y = y;
            this.userName = userName;
            this.followers = followers;
        }

        // Check if a point is within the bounds of the node
        boolean contains(Point point) {
            return new Rectangle(x - 30, y - 30, 60, 60).contains(point);
        }

        // Draw the node on the graphics context
        void draw(Graphics g) {
            if (isSelected) {
                g.setColor(Color.blue); // Change color for selected node
            } else {
                g.setColor(Color.lightGray);
            }
            g.fillOval(x - 30, y - 30, 60, 60);
            g.setColor(Color.black);
            g.drawString(userName + " (" + followers + " followers)", x - 30, y + 50);
            if (profileImage != null) {
                int imageSize = 40;
                g.drawImage(profileImage, x - imageSize / 2, y - imageSize / 2, imageSize, imageSize, null);
            }
        }
    }

    // Private class representing an edge between nodes
    private class Edge {
        Node startNode, endNode;
        String connectionStrength;

        boolean isSelected = false;

        // Constructor for creating an edge
        Edge(Node startNode, Node endNode, String connectionStrength) {
            this.startNode = startNode;
            this.endNode = endNode;
            this.connectionStrength = connectionStrength;
        }

        // Check if a point is near the edge
        boolean contains(Point point) {
            return new Line2D.Double(startNode.x, startNode.y, endNode.x, endNode.y).ptLineDist(point) < 5;
        }

        // Draw the edge on the graphics context
        void draw(Graphics g) {
            if (isSelected) {
                g.setColor(Color.red); // Change color for selected edge
            } else {
                g.setColor(Color.black);
            }

            g.drawLine(startNode.x, startNode.y, endNode.x, endNode.y);
            g.drawString(connectionStrength, (startNode.x + endNode.x) / 2 - 20, (startNode.y + endNode.y) / 2 + 20);
        }
    }

    // Draw grid lines on the graphics context
    private void drawGrid(Graphics g) {
        g.setColor(Color.lightGray);
        for (int x = 0; x < getWidth(); x += GRID_SIZE) {
            g.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += GRID_SIZE) {
            g.drawLine(0, y, getWidth(), y);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600); // Set preferred size of the panel
    }
}