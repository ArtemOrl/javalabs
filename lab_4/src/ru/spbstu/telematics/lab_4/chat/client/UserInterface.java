package ru.spbstu.telematics.lab_4.chat.client;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Queue;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ru.spbstu.telematics.lab_4.chat.client.ClientEvent.EventTypes;
import ru.spbstu.telematics.lab_4.chat.common.ChatMessage;
import ru.spbstu.telematics.lab_4.chat.common.ClientInfo;

/**
 * Класс, реализующий пользовательский интерфейс 
 * и взаимодействие с ним.
 * @author simonenko
 */
public class UserInterface extends JFrame {

   private static final long serialVersionUID = 2038878936378453669L;
   
   private final static int WINDOW_HEIGHT = 480;
   private final static int WINDOW_WIDTH = 640;
   private final static int DIALOG_HEIGHT = 125;
   private final static int DIALOG_WIDTH = 250;

   private JTextArea chatArea;                          // Текстовое поле для вывода сообщений чата.
   private JTextArea inputArea;                         // Текстовое поле для ввода сообщений чата.
   private JButton sendButton;                          // Кнопка отправки сообщения.
   private JButton enterButton;                         // Кнопка входа в чат - откытие диалогового окна.
   private JButton exitButton;                          // Кнопка выхода.
   private JPanel buttonPanel;                          // Панель, на которой располагаются кнопки.
   private JList usersList;                             // Список пользователей в чате.
   private JPanel interfacePanel;                       // Общая панель интерфейса, на которой располагаются остальные компоненты.
   private ActionListener shutdownListener;             // Обработчик события выхода из чата.
   private ActionListener sendListener;                 // Обработчик события отправки сообщений.
   private volatile Queue<ClientEvent> controllerQueue; // Очередь сообщений для передача контроллеру.
   private volatile Object controllerQueueMutex;        // Мьютекс для синхронизации очереди.
   private EnterDialog enterDialog;                     // Диалговое окно входя в чат.
   private int activeUserIndex;                         // Индекс пользователя вчбранного в списке.
   private boolean isActive;                            // Флаг активности интерфейса.
   
   /**
    * Конструктор класса
    */
   public UserInterface(Controller controller) {
      super();
      
      isActive = true;
      
      controllerQueue = controller.getQueue();
      controllerQueueMutex = controller.getQueueMutex();
      controller.setUI(this);
      
      activeUserIndex = -1;
      
      setResizable(false);
      setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      interfacePanel = new JPanel(new GridBagLayout());
      getContentPane().add(interfacePanel);
      
      makeLabel(interfacePanel, 
            new GridBagConstraints(0, 0, 2, 1, 2.8, 0.1, GridBagConstraints.WEST, GridBagConstraints.NONE, 
                  new Insets(2, 2, 2, 2), 0, 0), 
            "Chat window:");
      makeLabel(interfacePanel, 
            new GridBagConstraints(2, 0, 1, 1, 0.2, 0.1, GridBagConstraints.WEST, GridBagConstraints.NONE, 
                  new Insets(2, 2, 2, 2), 0, 0),
            "Users online:");
      makeLabel(interfacePanel,
            new GridBagConstraints(0, 2, 2, 1, 2.8, 0.1, GridBagConstraints.WEST, GridBagConstraints.NONE, 
                  new Insets(2, 2, 2, 2), 0, 0), 
            "Input window:");
      chatArea  = makeTextArea(interfacePanel, 
            new GridBagConstraints(0, 1, 2, 1, 2.8, 0.6, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 
                  new Insets(2, 2, 2, 2), 0, 0));
      inputArea = makeTextArea(interfacePanel, 
            new GridBagConstraints(0, 3, 2, 1, 2.8, 0.2, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 
                  new Insets(2, 2, 2, 2), 0, 0));
      usersList = makeUserList(interfacePanel, 
            new GridBagConstraints(2, 1, 1, 1, 0.2, 0.6, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 
                  new Insets(2, 2, 2, 2), 0, 0));
      buttonPanel = makePanel(interfacePanel, 
            new GridBagConstraints(2, 3, 1, 1, 0.2, 0.2, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 
                  new Insets(2, 2, 2, 2), 0, 0));
      sendButton = makeButton(buttonPanel, 
            new GridBagConstraints(0, 0, 1, 1, 1.0, 0.6, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                  new Insets(2, 2, 2, 2), 0, 0), 
            "Send");
      enterButton = makeButton(buttonPanel, 
            new GridBagConstraints(0, 1, 1, 1, 1.0, 0.2, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                  new Insets(2, 2, 2, 2), 0, 0),
            "Enter");
      exitButton = makeButton(buttonPanel, 
            new GridBagConstraints(0, 2, 1, 1, 1.0, 0.2, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                  new Insets(2, 2, 2, 2), 0, 0), 
            "Exit");
      
      sendButton.setEnabled(false);
      inputArea.setEditable(false);
      chatArea.setEditable(false);
      usersList.setEnabled(false);
      
      /**
       * Устанавливаем обработчик для кнопки входа в чат.
       * При нажатии вызывается диалоговое окно входа в чат.
       */
      enterButton.addActionListener(new ActionListener() {  
         @Override
         public void actionPerformed(ActionEvent arg0) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  UserInterface.this.enterDialog = 
                        new EnterDialog(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
                  UserInterface.this.enterButton.setEnabled(false);
                  UserInterface.this.enterDialog.setVisible(true);
               }
            });
         }
      });
      
      /**
       * Устанавливаем обработчик выбора элемента в списке.
       * При выборе элемента из списка запоминается его индекс, и вводимые сообщения
       * будут отправлятся этому пользователю.
       */
      usersList.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent arg0) {
            activeUserIndex = usersList.getSelectedIndex();
            if(activeUserIndex == -1)
               sendButton.setEnabled(false);
            else
               sendButton.setEnabled(true);
         }
      });
      
      sendListener = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ClientEvent event = new ClientEvent(EventTypes.SEND_MSG_EVENT);
            String message = UserInterface.this.inputArea.getText();
            event.setInfo(new ClientInfo(activeUserIndex));
            event.setMessage(message);
            synchronized (controllerQueueMutex) {
               UserInterface.this.controllerQueue.add(event);
            }
         }
      };
      
      shutdownListener = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            synchronized (controllerQueueMutex) {
               UserInterface.this.controllerQueue.add(
                     new ClientEvent(EventTypes.SHUTDOWN_EVENT));
            }
            isActive = false;
            UserInterface.shutdown(UserInterface.this);
         }
      };
      
      sendButton.addActionListener(sendListener);
      exitButton.addActionListener(shutdownListener);
      
      /**
       * Устанавливаем обработчик для оконных событий.
       */
      this.addWindowListener(new WindowListener() {
         @Override
         public void windowOpened(WindowEvent arg0) { }
         @Override
         public void windowIconified(WindowEvent arg0) { }
         @Override
         public void windowDeiconified(WindowEvent arg0) { }
         @Override
         public void windowDeactivated(WindowEvent arg0) { }
         @Override
         public void windowClosing(WindowEvent arg0) {
            if(isActive)
               shutdownListener.actionPerformed(null);
            isActive = false;
         }
         @Override
         public void windowClosed(WindowEvent arg0) { }
         @Override
         public void windowActivated(WindowEvent arg0) { }
      });
   }   
   
   /**
    * Статический метод создания label'а.
    * @param component - компонент, на котором будет распологаться label.
    * @param constraints - характеристики расположения label'a.
    * @param text - текст label'а.
    * @return ссылку на созданный label.
    */
   private static JLabel makeLabel(JComponent component, GridBagConstraints constraints, String text) {
      JLabel newLabel = new JLabel(text);
      component.add(newLabel, constraints);
      return newLabel;
   }

   /**
    * Статический метод создания текстового поля.
    * @param component - компонент, на котором будет распологаться текстовое поле.
    * @param constraints - характеристики расположения текстового поля.
    * @return ссылку на созданное текстовое поле.
    */
   private static JTextArea makeTextArea(JComponent component, GridBagConstraints constraints) {
      JTextArea newArea = new JTextArea();
      component.add(newArea, constraints);
      JScrollPane scroller = new JScrollPane(newArea,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      component.add(scroller, constraints);
      newArea.setLineWrap(true);
      return newArea;
   }
   
   /**
    * Статический метод создания списка (пользователей).
    * @param component - компонент, на котором будет распологаться список.
    * @param constraints - характеристики расположения списка.
    * @return ссылку на созданный список.
    */
   private static JList makeUserList(JComponent component, GridBagConstraints constraints) {
      JList newList = new JList();
      component.add(newList, constraints);
      JScrollPane scroller = new JScrollPane(newList,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      component.add(scroller, constraints);
      return newList;
   }
   
   /**
    * Статический метод создания панели.
    */
   private static JPanel makePanel(JComponent component, GridBagConstraints constraints) {
      JPanel newPanel = new JPanel(new GridBagLayout());
      component.add(newPanel, constraints);
      return newPanel;
   }

   /**
    * Статический метод создания кнопки.
    * @param text - надпись на кнопке.
    */
   private static JButton makeButton(JComponent component, GridBagConstraints constraints, String text) {
      JButton newButton = new JButton(text);
      component.add(newButton, constraints);
      return newButton;
   }
   
   /**
    * Статический метод создания текстовой строки ввода.
    * @param defaultText - текст, который будет выведен в строке при старте.
    */
   private static JTextField makeTextField(JComponent component, GridBagConstraints constraints, 
         String defaultText) {
      JTextField newTextField = new JTextField(defaultText);
      component.add(newTextField, constraints);
      return newTextField;
   }
   
   /**
    * Метод запуска отображения интерфейса.
    */
   public void run() {
      setVisible(true);
   }

   /**
    * Метод закрытия окна.
    * @param window - окно, которое должно получить событие, 
    * вызывающее зактрыие этого окна.
    */
   public synchronized static void shutdown(Window window) {
      window.getToolkit().getSystemEventQueue().postEvent(
            new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
   }
   
   /**
    * Метод, вызываемый после входа в чат.
    * @param lResult - результат входа в чат (true - вошел/false - не смог войти).
    * @param message - сообщение, выводимое в текстовое поле чата.
    */
   public synchronized void showLoginResult(boolean lResult, String message) {
      if(lResult) {
         enterDialog.loginButton.setText("OK");
         enterDialog.loginButton.removeActionListener(enterDialog.loginListener);
         enterDialog.loginButton.addActionListener(enterDialog.loginConfirmListener);
         enterDialog.loginButton.setEnabled(true);
      }
      else {
         enterDialog.loginButton.setText("FAIL");
         enterDialog.doneButton.setEnabled(true);
      }
      chatArea.append(message);
   }
   
   /**
    * Метод обновления списка пользователей.
    * @param userListData - новый список.
    * @param additionMessage - дополнительное сообщение, 
    * выводимое в текстовое поле чата.
    */
   public synchronized void updateUsersList(Vector<String> userListData, String additionMessage) {
      if(additionMessage != null)
         chatArea.append("\n" + additionMessage);
      usersList.setListData(userListData);
   }
   
   /**
    * Метод активации интерфейса. 
    * Переводит в активное состояние основные компонеты.
    */
   public synchronized void activateInterface() {
      inputArea.setEditable(true);
      usersList.setEnabled(true);
      enterButton.setEnabled(false);
   }
   
   /**
    * Метод получения индекса пользователя, выбранного в списке.
    */
   public synchronized int getActiveUserIndex() {
      return activeUserIndex;
   }
   
   /**
    * Метод вывода принятого сообщения в текстовое поле чата.
    * @param message - новое сообщение.
    */
   public void printMessage(ChatMessage message) {
      chatArea.append("\n[" + message.getFrom().getName() + 
            "]: " + message.getMessage());
      inputArea.setText("");
   }
   
   /**
    * Класс, описывающий диалоговое окно входа в чат.
    * @author simonenko
    */
   private class EnterDialog extends JDialog {

      private static final long serialVersionUID = 355011624412157471L;
      
      private JButton loginButton;                 // Кнопка входа.
      private JButton doneButton;                  // Кнопка закрытия диалогового окна.
      private JPanel buttonPanel;                  // Панель кнопок.
      private JTextField nameField;                // Текстовое поле ввода имени пользователя.
      private ActionListener loginListener;        // Обработчик события входа.
      private ActionListener cancelListener;       // Обработчик события закрытия окна.
      private ActionListener loginConfirmListener; // Обработчик события успешного входа в чат.

      /**
       * Конструктор класса.
       * @param size - размер диалогового окна.
       */
      public EnterDialog(Dimension size) {
         super(UserInterface.this, "Login to chat");
         setSize(size);
         setResizable(false);
         setTitle("Chat Client");
         setLocationRelativeTo(this);
         setDefaultCloseOperation(DISPOSE_ON_CLOSE);
         
         JPanel dialogPanel = new JPanel(new GridBagLayout());
         makeLabel(dialogPanel, 
               new GridBagConstraints(0, 1, 1, 1, 0.2, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, 
                     new Insets(2, 5, 2, 2), 0, 0), 
               "Login");
         nameField = makeTextField(dialogPanel, 
               new GridBagConstraints(1, 1, 2, 1, 1.8, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, 
                     new Insets(2, 2, 2, 2), 0, 0), 
               "");
         buttonPanel = makePanel(dialogPanel,
               new GridBagConstraints(0, 2, 0, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
                     new Insets(2, 2, 2, 2), 0, 0));
         loginButton = makeButton(buttonPanel, 
               new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, 
                     new Insets(2, 2, 2, 2), 0, 0), 
               "Login");
         doneButton = makeButton(buttonPanel, 
               new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, 
                     new Insets(2, 2, 2, 2), 0, 0), 
               "Cancel");
         
         loginListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
               EnterDialog.this.loginButton.setText("Wait a moment");
               EnterDialog.this.loginButton.setEnabled(false);
               EnterDialog.this.doneButton.setEnabled(false);
               ClientEvent event = new ClientEvent(ClientEvent.EventTypes.LOGIN_REQUEST);
               event.setMessage(EnterDialog.this.nameField.getText());
               synchronized (UserInterface.this.controllerQueueMutex) {
                  UserInterface.this.controllerQueue.add(event);
               }
            }
         };

         cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
               UserInterface.this.enterButton.setEnabled(true);
               UserInterface.shutdown(EnterDialog.this);

            }
         };
         
         loginConfirmListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               UserInterface.shutdown(EnterDialog.this);
               UserInterface.this.enterButton.setEnabled(false);
               UserInterface.this.activateInterface();
               System.out.println("login-gonfirm");
            }
         };
         
         loginButton.addActionListener(loginListener);
         doneButton.addActionListener(cancelListener);
         add(dialogPanel);
         
         this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent arg0) { }
            @Override
            public void windowIconified(WindowEvent arg0) { }
            @Override
            public void windowDeiconified(WindowEvent arg0) { }
            @Override
            public void windowDeactivated(WindowEvent arg0) { }
            @Override
            public void windowClosing(WindowEvent arg0) {
               cancelListener.actionPerformed(null);
            }
            @Override
            public void windowClosed(WindowEvent arg0) { }
            @Override
            public void windowActivated(WindowEvent arg0) { }
         });
      }
   }
}

