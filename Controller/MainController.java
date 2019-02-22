package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.xml.ws.handler.MessageContext;
import Model.InOutcomeList;
import Model.InOutcomeListDAO;
import Model.UserInformationDAO;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {
	public Stage mainStage;
	public String io_userID;

	public void setIouserID(String iouserID) {
		this.io_userID = iouserID;
	}

	@FXML
	Button btnRefresh;// ���ΰ�ħ
	@FXML
	TextField iouserID;
	@FXML
	Button dateSearch; // �˻�
	@FXML
	Button iolBtnExit;// ����
	@FXML
	TextField presentTime;// ����ð�
	@FXML
	TextField presentDate;// ���糯¥
	@FXML
	DatePicker iolDatePicker;// ��������Ŀ(��¥�� ��)
	@FXML
	TextField iolTxtBalance;// �ܵ�(0��)
	@FXML
	Button iolBtnIncome;// ����
	@FXML
	Button iolBtnOutcome;// ����
	@FXML
	Button iolBtnEdit;// ����
	@FXML
	Button iolBtnPiechart;// ������Ʈ(�̸�Ƽ��)

	private InOutcomeList selectInOutcomeList;
	private int selectInOutcomeIndex;

	@FXML
	TableView<InOutcomeList> iolTableview;// ���������������̺��
	ObservableList<InOutcomeList> iolData = FXCollections.observableArrayList();// ?
	ObservableList<InOutcomeList> dateData = FXCollections.observableArrayList();// ?
	ArrayList<InOutcomeList> iolArrayList;
	ArrayList<InOutcomeList> dateArrayList;
	ObservableList<String> group = FXCollections.observableArrayList();// ��з��׷��� �ı����� ��Ƶа͵�
	ObservableList<String> group1 = FXCollections.observableArrayList();// �Һз��׷��� �ı����� ��Ƶа͵�
	ObservableList<String> group2 = FXCollections.observableArrayList();// �����׷��� �ı����� ��Ƶа͵�

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 0.0 ó�� ȭ�� �⺻ ���� �Լ�
		settingFirstScene();

		// 0.1 ����Ʈ��Ŀ�˻��� ��������
		dateSearch.setOnAction(e -> {
			datePickerPropertyTableView();
		});

		// 0. ���̺�並 �⺻������ ����
		Connection con = DBUtility.getConnection();

		// 0. ����-���� ��� ���̺�� ����
		setInOutcomeListTab1TableView();

		// 1. ���Թ�ư�� �������� ����â�� ��
		iolBtnIncome.setOnAction((e) -> {
			handlebtnIncomeAction();
		});

		// 2. �����ư�� �������� ����â�� ��
		iolBtnOutcome.setOnAction((e) -> {
			handlebtnOutcomeAction();
		});

		// 3. ���Կ����� ���̺���Ʈ�� ������ ���Լ����� ��, ���⿡ ���� ���̺���Ʈ�� ������ ��������� ��
		iolBtnEdit.setOnAction((MouseEvente) -> {
			handlebtnEditAction();
		});

		// 4. ��(�̸�Ƽ��)��ư�� ������ ��Ʈâ�� ��
		iolBtnPiechart.setOnAction((e) -> {
			handlebtnPiechartAction();
		});

		// 5. �����ư�� �������� ����
		iolBtnExit.setOnAction((e) -> {
			Platform.exit();
		});

		// 6. ���̺�� ���� ������ ->�ι������ϸ� ����
		iolTableview.setOnMouseClicked((e) -> {
			selectTableListTwoClickRemove(e);
		});

		// 7. ���ΰ�ħ������ ��� ���糯¥, �ð�, ���̺��, �ܾ��� ����������
		btnRefresh.setOnAction((e) -> {

			settingFirstScene();
			if(!iolData.isEmpty()&& !iolArrayList.isEmpty()) {
				iolData.clear();
				iolArrayList.clear();
			}
			iolArrayList = InOutcomeListDAO.getInOutcomeList();
			for(InOutcomeList iocl :iolArrayList) {
				iolData.add(iocl);
			}
			iolTableview.setItems(iolData);
			iolDatePicker.setValue(null);

		});

	}

	// 0.1 ����Ʈ��Ŀ�� ����ɶ� ���̺�� ����
	private void datePickerPropertyTableView() {
		dateData.clear();
		dateArrayList = InOutcomeListDAO.getDateList(iolDatePicker.getValue().toString(), io_userID);
		for (InOutcomeList ioc : dateArrayList) {
			dateData.add(ioc);
		}
		iolTableview.refresh();
		iolTableview.setItems(dateData);
	}

	// 0-0 ó�� ȭ�� �⺻ ���� �Լ�
	private void settingFirstScene() {
		LocalDate currentDate = LocalDate.now();
		LocalTime currentTime = LocalTime.now();
		presentDate.setText(currentDate.toString());
		presentTime.setText(currentTime.toString().substring(0, 5));

		// ���ܾ� ǥ��
		int income = InOutcomeListDAO.balanceIncome(io_userID);
		int outcome = InOutcomeListDAO.balanceOutcome(io_userID);

		int total = income - outcome;
		iolTxtBalance.setText(total + "��");

	}

//0. ����-���� ��� ���̺�� ����
	private void setInOutcomeListTab1TableView() {

		// ��ȣ
		TableColumn tcNo = iolTableview.getColumns().get(0);
		tcNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		tcNo.setStyle("-fx-alignment: CENTER;");
		// �Ƶ�
		TableColumn tcUserid = iolTableview.getColumns().get(1);
		tcUserid.setCellValueFactory(new PropertyValueFactory<>("userid"));
		tcUserid.setStyle("-fx-alignment: CENTER;");

		// ��¥
		TableColumn tcDate = iolTableview.getColumns().get(2);
		tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		tcDate.setStyle("-fx-alignment: CENTER;");

		// �ð�
		TableColumn tcHourminute = iolTableview.getColumns().get(3);
		tcHourminute.setCellValueFactory(new PropertyValueFactory<>("hourminute"));
		tcHourminute.setStyle("-fx-alignment: CENTER;");

		// ����
		TableColumn tcIncome = iolTableview.getColumns().get(4);
		tcIncome.setCellValueFactory(new PropertyValueFactory<>("income"));
		tcIncome.setStyle("-fx-alignment: CENTER;");

		// ����
		TableColumn tcOutcome = iolTableview.getColumns().get(5);
		tcOutcome.setCellValueFactory(new PropertyValueFactory<>("outcome"));
		tcOutcome.setStyle("-fx-alignment: CENTER;");

		// ��з�
		TableColumn tcBiggroup = iolTableview.getColumns().get(6);
		tcBiggroup.setCellValueFactory(new PropertyValueFactory<>("biggroup"));
		tcBiggroup.setStyle("-fx-alignment: CENTER;");

		// �Һз�
		TableColumn tcSmallgroup = iolTableview.getColumns().get(7);
		tcSmallgroup.setCellValueFactory(new PropertyValueFactory<>("smallgroup"));
		tcSmallgroup.setStyle("-fx-alignment: CENTER;");

		// ����
		TableColumn tcPayment = iolTableview.getColumns().get(8);
		tcPayment.setCellValueFactory(new PropertyValueFactory<>("payment"));
		tcPayment.setStyle("-fx-alignment: CENTER;");

		// ����
		TableColumn tcContents = iolTableview.getColumns().get(9);
		tcContents.setCellValueFactory(new PropertyValueFactory<>("contents"));
		tcContents.setStyle("-fx-alignment: CENTER;");

		iolArrayList = InOutcomeListDAO.getInOutcomeList();

		for (InOutcomeList inOutcomeList : iolArrayList) {
			iolData.add(inOutcomeList);
		}
		iolTableview.setItems(iolData);

	}

	// 1. ���Թ�ư�� �������� ����â�� ��
	private void handlebtnIncomeAction() {
		try {
			// â�� ���� ����
			Stage IOStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/Income.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			IOStage.initModality(Modality.WINDOW_MODAL);
			IOStage.initOwner(mainStage);
			IOStage.setScene(scene);
			IOStage.setTitle("Income");
			IOStage.setResizable(false);
			IOStage.show();

			// â�ȿ��� Action
			Button ICjujang = (Button) root.lookup("#icBtnJujang");// �����ư����
			Button ICcancel = (Button) root.lookup("#icBtnCancel");// ��ҹ�ư����
			Button btnClearIncome = (Button) root.lookup("#btnClearIncome");// �ʱ�ȭ��ư����

			ComboBox<String> incomeBiggroup = (ComboBox) root.lookup("#incomeBiggroup");// �޺��ڽ���з��׷쿬��
			ComboBox<String> incomePaymentgroup = (ComboBox) root.lookup("#incomePaymentgroup");// �޺��ڽ������׷쿬��
			ComboBox<String> incomeSmallgroup = (ComboBox) root.lookup("#incomeSmallgroup");// �޺��ڽ��Һз��׷쿬��
			TextField incomeWon = (TextField) root.lookup("#incomeWon");// ���Աݾ��Է��ؽ�Ʈ�ʵ忬��
			TextField incomeT1 = (TextField) root.lookup("#incomeT1");// ��
			TextField incomeT2 = (TextField) root.lookup("#incomeT2");// ��
			TextField incomeContent = (TextField) root.lookup("#incomeContent");// ����

			ICjujang.setOnAction((e) -> {

				InOutcomeList inoutcomeList = new InOutcomeList(null, io_userID,
						Date.valueOf(iolDatePicker.getValue().toString()),
						incomeT1.getText() + ":" + incomeT2.getText(), incomeWon.getText(), null,
						incomeBiggroup.getSelectionModel().getSelectedItem(),
						incomeSmallgroup.getSelectionModel().getSelectedItem(),
						incomePaymentgroup.getSelectionModel().getSelectedItem(), incomeContent.getText());

				int count = InOutcomeListDAO.insertInOutcome(inoutcomeList);
				iolData.add(inoutcomeList);
				IOStage.close();

			});

			group.clear();
			group.addAll("����", "�ٷμҵ�", "�����ҵ�", "��Ÿ");// �׷�ȿ� �ı���
			group2.clear();
			group2.addAll("����", "üũī��", "�ſ�ī��", "��Ÿ");

			// ��з�->�Һз� �޺��ڽ�

			incomeBiggroup.setItems(group);// �޺��ڽ��� �ı����� �ְڴ�
			incomePaymentgroup.setItems(group2);

			// �Һз�Ŭ��������
			incomeSmallgroup.setOnMouseClicked((e) -> {
				incomeBiggroup.setDisable(true);// ��з� ���ø���

				// ��1) ��з��� �ٷμҵ漱��->�Һз��� ���� �޿� ���ʽ��� ��
				// ��2) ��з��� �����ҵ漱��->�Һз��� ���� ���� ������ ��

				switch (incomeBiggroup.getSelectionModel().getSelectedItem()) {
				case "����":
					group1.clear();
					group1.addAll("����");// �׷�ȿ� �ı���
					incomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "�ٷμҵ�":
					group1.clear();
					group1.addAll("����", "�޿�", "���ʽ�");// �׷�ȿ� �ı���
					incomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "�����ҵ�":
					group1.clear();
					group1.addAll("����", "����", "����");// �׷�ȿ� �ı���
					incomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "��Ÿ":
					group1.clear();
					group1.addAll("����");// �׷�ȿ� �ı���
					incomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				default:
					break;
				}
			});

			// �ʱ�ȭ��ư
			btnClearIncome.setOnAction((e) -> {
				incomeWon.clear();
				incomeT1.clear();
				incomeT2.clear();
				incomeBiggroup.getSelectionModel().clearSelection();
				incomeSmallgroup.getSelectionModel().clearSelection();
				incomePaymentgroup.getSelectionModel().clearSelection();
				incomeContent.clear();
			});

			ICcancel.setOnAction((e) -> {
				IOStage.close();
			}); // ��Ҵ����� â�ݱ�

		} catch (Exception e) {
			callAlert("ȭ����ȯ����:ȭ����ȯ�� ������ �ֽ��ϴ�. ����ٶ�.");
		}
	}

	// 2. �����ư�� �������� ����â�� ��
	private void handlebtnOutcomeAction() {
		try {
			// â�� ���� ����
			Stage IOStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/Outcome.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			IOStage.initModality(Modality.WINDOW_MODAL);
			IOStage.initOwner(mainStage);
			IOStage.setScene(scene);
			IOStage.setTitle("Outcome");
			IOStage.setResizable(false);
			IOStage.show();

			// â�ȿ��� Action
			Button OCjujang = (Button) root.lookup("#ocBtnJujang");// �����ư����
			Button OCcancel = (Button) root.lookup("#ocBtnCancel");// ��ҹ�ư����
			Button btnClearOutcome = (Button) root.lookup("#btnClearOutcome");// ��ư����
			ComboBox<String> outcomeBiggroup = (ComboBox) root.lookup("#outcomeBiggroup");// �޺��ڽ���з��׷쿬��
			ComboBox<String> outcomePaymentgroup = (ComboBox) root.lookup("#outcomePaymentgroup");// �޺��ڽ������׷쿬��
			ComboBox<String> outcomeSmallgroup = (ComboBox) root.lookup("#outcomeSmallgroup");// �޺��ڽ��Һз��׷쿬��
			TextField outcomeWon = (TextField) root.lookup("#outcomeWon");// ���Աݾ��Է��ؽ�Ʈ�ʵ忬��
			TextField outcomeT1 = (TextField) root.lookup("#outcomeT1");// ��
			TextField outcomeT2 = (TextField) root.lookup("#outcomeT2");// ��
			TextField outcomeContent = (TextField) root.lookup("#outcomeContent");// ����

			OCjujang.setOnAction((e) -> {

				InOutcomeList inoutcomeList = new InOutcomeList(null, io_userID,
						Date.valueOf(iolDatePicker.getValue().toString()),
						outcomeT1.getText() + ":" + outcomeT2.getText(), null, outcomeWon.getText(),
						outcomeBiggroup.getSelectionModel().getSelectedItem(),
						outcomeSmallgroup.getSelectionModel().getSelectedItem(),
						outcomePaymentgroup.getSelectionModel().getSelectedItem(), outcomeContent.getText());
				int count = InOutcomeListDAO.insertInOutcome(inoutcomeList);
				iolData.add(inoutcomeList);
				IOStage.close();
			});

			group.clear();
			group.addAll("����", "�ĺ�", "��ȭ��Ȱ��", "�ְŻ�Ȱ��", "�ǰ�������", "�����", "����������", "���κ�", "�̿��", "������", "��ȸ��Ȱ��", "�����",
					"���������", "����", "��Ÿ");// �׷�ȿ� �ı���
			group2.clear();
			group2.addAll("����", "üũī��", "�ſ�ī��", "��Ÿ");

			// ��з�->�Һз� �޺��ڽ�

			outcomeBiggroup.setItems(group);// �޺��ڽ��� �ı����� �ְڴ�
			outcomePaymentgroup.setItems(group2);

			// �Һз�Ŭ��������
			outcomeSmallgroup.setOnMouseClicked((e) -> {
				outcomeBiggroup.setDisable(true);// ��з� ���ø���

				// ��1) ��з��� �ٷμҵ漱��->�Һз��� ���� �޿� ���ʽ��� ��
				// ��2) ��з��� �����ҵ漱��->�Һз��� ���� ���� ������ ��

				switch (outcomeBiggroup.getSelectionModel().getSelectedItem()) {
				case "����":
					group1.clear();
					group1.addAll("����");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "�ĺ�":
					group1.clear();
					group1.addAll("����", "�Ļ�/����", "Ŀ��/����", "�����");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "��ȭ��Ȱ��":
					group1.clear();
					group1.addAll("����", "��ȭ/����", "����/����", "����", "����", "����", "���");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "�ְŻ�Ȱ��":
					group1.clear();
					group1.addAll("����", "����/������", "û��/��Ź", "��ź�", "����ǰ", "��Ȱ����", "��Ȱ����");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "�ǰ�������":
					group1.clear();
					group1.addAll("����", "�/���̾�Ʈ", "����/�ప", "����", "�ǰ���ǰ");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "�����":
					group1.clear();
					group1.addAll("����", "���߱���", "�ýú�", "��Ÿ����", "��Ʈ��");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "����������":
					group1.clear();
					group1.addAll("����", "������", "����/����", "����/����", "�ڵ�������");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "���κ�":
					group1.clear();
					group1.addAll("����", "�Ƿ�/��ȭ", "������ǰ", "����");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "�̿��":
					group1.clear();
					group1.addAll("����", "��", "ȭ��ǰ", "��Ƽ����");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "������":
					group1.clear();
					group1.addAll("����", "�к�", "�����", "����");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "��ȸ��Ȱ��":
					group1.clear();
					group1.addAll("����", "�������", "����/�뵷", "����ȸ��", "���");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "�����":
					group1.clear();
					group1.addAll("����", "��ȭ/����", "����/����", "����", "����", "����", "���");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "���������":
					group1.clear();
					group1.addAll("����", "��������", "������", "���庸��");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "����":
					group1.clear();
					group1.addAll("����", "����/����", "�ֽ�/�ݵ�", "���ຸ��");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				case "��Ÿ":
					group1.clear();
					group1.addAll("����");// �׷�ȿ� �ı���
					outcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
					break;
				default:
					break;
				}
			});

			// �ʱ�ȭ��ư
			btnClearOutcome.setOnAction((e) -> {
				outcomeWon.clear();
				outcomeT1.clear();
				outcomeT2.clear();
				outcomeBiggroup.getSelectionModel().clearSelection();
				outcomeSmallgroup.getSelectionModel().clearSelection();
				outcomePaymentgroup.getSelectionModel().clearSelection();
				outcomeContent.clear();
			});

			OCcancel.setOnAction((e) -> {
				IOStage.close();
			}); // ��Ҵ����� â�ݱ�

		} catch (Exception e) {
			callAlert("ȭ����ȯ����:ȭ����ȯ�� ������ �ֽ��ϴ�. ����ٶ�.");

		}
	}

	// 3. ���Կ����� ���̺���Ʈ�� ������ ���Լ����� ��, ���⿡ ���� ���̺���Ʈ�� ������ ��������� ��
	private void handlebtnEditAction() {

		// ���� ������ ���̺���Ʈ�� ���̺��ݷ��� ������ null�϶�, �����϶�
		if (iolTableview.getSelectionModel().getSelectedItem().getIncome() == null) {
			try {
				// â�� ���� ����
				Stage outStage = new Stage();
				FXMLLoader outLoader = new FXMLLoader(getClass().getResource("../View/OutcomeEdit.fxml"));
				Parent outRoot = outLoader.load();
				Scene scene = new Scene(outRoot);
				outStage.initModality(Modality.WINDOW_MODAL);
				outStage.initOwner(mainStage);
				outStage.setScene(scene);
				outStage.setTitle("OutcomeEdit");
				outStage.setResizable(false);
				outStage.show();

				// â�ȿ��� Action
				Button eoBtnJujang = (Button) outRoot.lookup("#eoBtnJujang");// �����ư����
				Button eoBtnCancel = (Button) outRoot.lookup("#eoBtnCancel");// ��ҹ�ư����
				Button btnEoutcomeClear = (Button) outRoot.lookup("#btnEoutcomeClear");// ��ҹ�ư����
				ComboBox<String> EOutcomeBiggroup = (ComboBox) outRoot.lookup("#EOutcomeBiggroup");// �޺��ڽ���з��׷쿬��
				ComboBox<String> EOutcomePaymentgroup = (ComboBox) outRoot.lookup("#EOutcomePaymentgroup");// �޺��ڽ������׷쿬��
				ComboBox<String> EOutcomeSmallgroup = (ComboBox) outRoot.lookup("#EOutcomeSmallgroup");// �޺��ڽ��Һз��׷쿬��
				TextField EOutcomeWon = (TextField) outRoot.lookup("#EOutcomeWon");// ���Աݾ��Է��ؽ�Ʈ�ʵ忬��
				TextField EOutcomeT1 = (TextField) outRoot.lookup("#EOutcomeT1");// ��
				TextField EOutcomeT2 = (TextField) outRoot.lookup("#EOutcomeT2");// ��
				TextField EOutcomeContent = (TextField) outRoot.lookup("#EOutcomeContent");// ����

				selectInOutcomeList = iolTableview.getSelectionModel().getSelectedItem();
				int index = iolTableview.getSelectionModel().getSelectedIndex();
				EOutcomeWon.setText(selectInOutcomeList.getOutcome());
				String[] hourminute = selectInOutcomeList.getHourminute().split(":");
				String hour = hourminute[0];
				String minute = hourminute[1];
				EOutcomeT1.setText(hour);
				EOutcomeT2.setText(minute);
				EOutcomeBiggroup.getSelectionModel().select(selectInOutcomeList.getBiggroup());
				EOutcomeSmallgroup.getSelectionModel().select(selectInOutcomeList.getSmallgroup());
				EOutcomePaymentgroup.getSelectionModel().select(selectInOutcomeList.getPayment());
				EOutcomeContent.setText(selectInOutcomeList.getContents());

				// ������
				eoBtnJujang.setOnAction((e) -> {

					String outcome = EOutcomeWon.getText().trim();
					String t1 = EOutcomeT1.getText().trim();
					String t2 = EOutcomeT2.getText().trim();
					String biggroup = EOutcomeBiggroup.getSelectionModel().getSelectedItem().trim();
					String smallgroup = EOutcomeSmallgroup.getSelectionModel().getSelectedItem().trim();
					String payment = EOutcomePaymentgroup.getSelectionModel().getSelectedItem().trim();
					String contents = EOutcomeContent.getText().trim();

					InOutcomeList inoutcomeList = new InOutcomeList(selectInOutcomeList.getNo(),
							selectInOutcomeList.getUserid(), selectInOutcomeList.getDate(),
							EOutcomeT1.getText() + ":" + EOutcomeT2.getText(), null, EOutcomeWon.getText(),
							EOutcomeBiggroup.getSelectionModel().getSelectedItem(),
							EOutcomeSmallgroup.getSelectionModel().getSelectedItem(),
							EOutcomePaymentgroup.getSelectionModel().getSelectedItem(), EOutcomeContent.getText());
					int count = InOutcomeListDAO.updateInOutcomeData(inoutcomeList);
					if (count != 0) {
						callAlert("���� ����!:^^");
						iolArrayList.set(index, selectInOutcomeList);
						for (InOutcomeList io : iolArrayList) {
							dateData.add(io);
						}
						iolTableview.setItems(dateData);
					} else {
						callAlert("���� ����!:T^T");
					}

				});

				group.clear();
				group.addAll("����", "�ĺ�", "��ȭ��Ȱ��", "�ְŻ�Ȱ��", "�ǰ�������", "�����", "����������", "���κ�", "�̿��", "������", "��ȸ��Ȱ��", "�����",
						"���������", "����", "��Ÿ");// �׷�ȿ� �ı���
				group2.clear();
				group2.addAll("����", "üũī��", "�ſ�ī��", "��Ÿ");

				// ��з�->�Һз� �޺��ڽ�

				EOutcomeBiggroup.setItems(group);// �޺��ڽ��� �ı����� �ְڴ�
				EOutcomePaymentgroup.setItems(group2);

				// �Һз�Ŭ��������
				EOutcomeSmallgroup.setOnMouseClicked((e) -> {
					EOutcomeBiggroup.setDisable(true);// ��з� ���ø���

					// ��1) ��з��� �ٷμҵ漱��->�Һз��� ���� �޿� ���ʽ��� ��
					// ��2) ��з��� �����ҵ漱��->�Һз��� ���� ���� ������ ��

					switch (EOutcomeBiggroup.getSelectionModel().getSelectedItem()) {
					case "����":
						group1.clear();
						group1.addAll("����");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "�ĺ�":
						group1.clear();
						group1.addAll("����", "�Ļ�/����", "Ŀ��/����", "�����");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "��ȭ��Ȱ��":
						group1.clear();
						group1.addAll("����", "��ȭ/����", "����/����", "����", "����", "����", "���");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "�ְŻ�Ȱ��":
						group1.clear();
						group1.addAll("����", "����/������", "û��/��Ź", "��ź�", "����ǰ", "��Ȱ����", "��Ȱ����");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "�ǰ�������":
						group1.clear();
						group1.addAll("����", "�/���̾�Ʈ", "����/�ప", "����", "�ǰ���ǰ");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "�����":
						group1.clear();
						group1.addAll("����", "���߱���", "�ýú�", "��Ÿ����", "��Ʈ��");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "����������":
						group1.clear();
						group1.addAll("����", "������", "����/����", "����/����", "�ڵ�������");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "���κ�":
						group1.clear();
						group1.addAll("����", "�Ƿ�/��ȭ", "������ǰ", "����");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "�̿��":
						group1.clear();
						group1.addAll("����", "��", "ȭ��ǰ", "��Ƽ����");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "������":
						group1.clear();
						group1.addAll("����", "�к�", "�����", "����");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "��ȸ��Ȱ��":
						group1.clear();
						group1.addAll("����", "�������", "����/�뵷", "����ȸ��", "���");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "�����":
						group1.clear();
						group1.addAll("����", "��ȭ/����", "����/����", "����", "����", "����", "���");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "���������":
						group1.clear();
						group1.addAll("����", "��������", "������", "���庸��");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "����":
						group1.clear();
						group1.addAll("����", "����/����", "�ֽ�/�ݵ�", "���ຸ��");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "��Ÿ":
						group1.clear();
						group1.addAll("����");// �׷�ȿ� �ı���
						EOutcomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					default:
						break;
					}
				});

				// �ʱ�ȭ��ư
				btnEoutcomeClear.setOnAction((e) -> {
					EOutcomeWon.clear();
					EOutcomeT1.clear();
					EOutcomeT2.clear();
					EOutcomeBiggroup.getSelectionModel().clearSelection();
					EOutcomeSmallgroup.getSelectionModel().clearSelection();
					EOutcomePaymentgroup.getSelectionModel().clearSelection();
					EOutcomeContent.clear();
				});

				eoBtnCancel.setOnAction((e) -> {
					outStage.close();
				}); // ��Ҵ����� â�ݱ�

			} catch (Exception e) {
				callAlert("ȭ����ȯ����:ȭ����ȯ�� ������ �ֽ��ϴ�. ����ٶ�.");

			}
			// ���� ������ ���̺���Ʈ�� ���̺��ݷ��� ������ null�϶�, �����϶�
		} else {
			try {
				// â�� ���� ����
				Stage incomeStage = new Stage();
				FXMLLoader incomeLoader = new FXMLLoader(getClass().getResource("../View/IncomeEdit.fxml"));
				Parent incomeRoot = incomeLoader.load();
				Scene incomeScene = new Scene(incomeRoot);
				incomeStage.initModality(Modality.WINDOW_MODAL);
				incomeStage.initOwner(mainStage);
				incomeStage.setScene(incomeScene);
				incomeStage.setTitle("IncomeEdit");
				incomeStage.setResizable(false);
				incomeStage.show();

				// â�ȿ��� Action
				Button eiBtnJujang = (Button) incomeRoot.lookup("#eiBtnJujang");// �����ư����
				Button eiBtnCancel = (Button) incomeRoot.lookup("#eiBtnCancel");// ��ҹ�ư����
				Button btnEincomeClear = (Button) incomeRoot.lookup("#btnEincomeClear");// ��ư����
				ComboBox<String> EIncomeBiggroup = (ComboBox) incomeRoot.lookup("#EIncomeBiggroup");// �޺��ڽ���з��׷쿬��
				ComboBox<String> EIncomePaymentgroup = (ComboBox) incomeRoot.lookup("#EIncomePaymentgroup");// �޺��ڽ������׷쿬��
				ComboBox<String> EIncomeSmallgroup = (ComboBox) incomeRoot.lookup("#EIncomeSmallgroup");// �޺��ڽ��Һз��׷쿬��
				TextField EIncomeWon = (TextField) incomeRoot.lookup("#EIncomeWon");// ���Աݾ��Է��ؽ�Ʈ�ʵ忬��
				TextField EIncomeT1 = (TextField) incomeRoot.lookup("#EIncomeT1");// ��
				TextField EIncomeT2 = (TextField) incomeRoot.lookup("#EIncomeT2");// ��
				TextField EIncomeContent = (TextField) incomeRoot.lookup("#EIncomeContent");// ����

				selectInOutcomeList = iolTableview.getSelectionModel().getSelectedItem();
				int index = iolTableview.getSelectionModel().getSelectedIndex();
				EIncomeWon.setText(selectInOutcomeList.getIncome());
				String[] hourminute = selectInOutcomeList.getHourminute().split(":");
				String hour = hourminute[0];
				String minute = hourminute[1];
				EIncomeT1.setText(hour);
				EIncomeT2.setText(minute);
				EIncomeBiggroup.getSelectionModel().select(selectInOutcomeList.getBiggroup());
				EIncomeSmallgroup.getSelectionModel().select(selectInOutcomeList.getSmallgroup());
				EIncomePaymentgroup.getSelectionModel().select(selectInOutcomeList.getPayment());
				EIncomeContent.setText(selectInOutcomeList.getContents());

				// ��������

				eiBtnJujang.setOnAction((e) -> {
					String income = EIncomeWon.getText().trim();
					String t1 = EIncomeT1.getText().trim();
					String t2 = EIncomeT2.getText().trim();
					String biggroup = EIncomeBiggroup.getSelectionModel().getSelectedItem().trim();
					String smallgroup = EIncomeSmallgroup.getSelectionModel().getSelectedItem().trim();
					String payment = EIncomePaymentgroup.getSelectionModel().getSelectedItem().trim();
					String contents = EIncomeContent.getText().trim();

					iolData.set(index, selectInOutcomeList);
					InOutcomeList inoutcomeList = new InOutcomeList(selectInOutcomeList.getNo(),
							selectInOutcomeList.getUserid(), selectInOutcomeList.getDate(),
							EIncomeT1.getText() + ":" + EIncomeT2.getText(), EIncomeWon.getText(), null,
							EIncomeBiggroup.getSelectionModel().getSelectedItem(),
							EIncomeSmallgroup.getSelectionModel().getSelectedItem(),
							EIncomePaymentgroup.getSelectionModel().getSelectedItem(), EIncomeContent.getText());
					int count = InOutcomeListDAO.updateInOutcomeData(inoutcomeList);
					if (count != 0) {
						callAlert("���� ����!:^^");
						iolArrayList.set(index, selectInOutcomeList);
						for (InOutcomeList io : iolArrayList) {
							dateData.add(io);
						}
						iolTableview.setItems(dateData);
					} else {
						callAlert("���� ����!:T^T");
					}
				});

				group.clear();
				group.addAll("����", "�ٷμҵ�", "�����ҵ�", "��Ÿ");// �׷�ȿ� �ı���
				group2.clear();
				group2.addAll("����", "üũī��", "�ſ�ī��", "��Ÿ");

				// ��з�->�Һз� �޺��ڽ�

				EIncomeBiggroup.setItems(group);// �޺��ڽ��� �ı����� �ְڴ�
				EIncomePaymentgroup.setItems(group2);

				// �Һз�Ŭ��������
				EIncomeSmallgroup.setOnMouseClicked((e) -> {
					EIncomeBiggroup.setDisable(true);// ��з� ���ø���

					// ��1) ��з��� �ٷμҵ漱��->�Һз��� ���� �޿� ���ʽ��� ��
					// ��2) ��з��� �����ҵ漱��->�Һз��� ���� ���� ������ ��

					switch (EIncomeBiggroup.getSelectionModel().getSelectedItem()) {
					case "����":
						group1.clear();
						group1.addAll("����");// �׷�ȿ� �ı���
						EIncomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "�ٷμҵ�":
						group1.clear();
						group1.addAll("����", "�޿�", "���ʽ�");// �׷�ȿ� �ı���
						EIncomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "�����ҵ�":
						group1.clear();
						group1.addAll("����", "����", "����");// �׷�ȿ� �ı���
						EIncomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					case "��Ÿ":
						group1.clear();
						group1.addAll("����");// �׷�ȿ� �ı���
						EIncomeSmallgroup.setItems(group1);// �޺��ڽ��� �ı����� �ְڴ�
						break;
					default:
						break;
					}
				});

				// �ʱ�ȭ��ư
				btnEincomeClear.setOnAction((e) -> {
					EIncomeWon.clear();
					EIncomeT1.clear();
					EIncomeT2.clear();
					EIncomeBiggroup.getSelectionModel().clearSelection();
					EIncomeSmallgroup.getSelectionModel().clearSelection();
					EIncomePaymentgroup.getSelectionModel().clearSelection();
					EIncomeContent.clear();
				});

				eiBtnCancel.setOnAction((e) -> {
					incomeStage.close();
				}); // ��Ҵ����� â�ݱ�

			} catch (Exception e) {

				callAlert("ȭ����ȯ����:ȭ����ȯ�� ������ �ֽ��ϴ�. ����ٶ�.");
			}
		} // end of if

	}// end of �Լ�

	// 4. ��(�̸�Ƽ��)��ư�� ������ ��Ʈâ�� ��
	private void handlebtnPiechartAction() {
		try {
			// â�� ���� ����
			Stage IOStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/Chartpane.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			IOStage.initModality(Modality.WINDOW_MODAL);
			IOStage.initOwner(mainStage);
			IOStage.setScene(scene);
			IOStage.setTitle("Pie chart");
			IOStage.setResizable(false);
			IOStage.show();

			// â�ȿ��� Action
			Button pieBtnClose = (Button) root.lookup("#pieBtnClose");// �ݱ��ư����
			ComboBox<String> cmbYearGroup = (ComboBox) root.lookup("#cmbYearGroup");// �⵵�޺��ڽ�����
			if (!group.isEmpty()) {
				group.clear();
			}
			group.addAll("2018", "2019", "2020", "2021");// �׷�ȿ� �ı���

			cmbYearGroup.setItems(group);// �޺��ڽ��� �ı����� �ְڴ�

			// ��Ʈ����
			/*************************** ��Ʈ�� ù����ȭ��(����) ****************************/
			Button chartIn = (Button) root.lookup("#chartIn");// ���Թ�ư
			Button chartOut = (Button) root.lookup("#chartOut");// �����ư
			Button chartInOut = (Button) root.lookup("#chartInOut");// ����-�����ư
			PieChart chartM = (PieChart) root.lookup("#chartM");// ������Ʈ
			Button chart2in = (Button) root.lookup("#chart2in");// ���Թ�ư
			Button chart2out = (Button) root.lookup("#chart2out");// �����ư
			PieChart chartC = (PieChart) root.lookup("#chartC");// �з�����Ʈ
			Button chart3in = (Button) root.lookup("#chart3in");// ���Թ�ư
			Button chart3out = (Button) root.lookup("#chart3out");// �����ư
			PieChart chartP = (PieChart) root.lookup("#chartP");// ����������Ʈ

			// ���Դ�������
			cmbYearGroup.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// ����
					chartIn.setOnAction((e) -> {
						int January = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "01");
						int Febrary = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "02");
						int March = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "03");
						int April = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "04");
						int May = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "05");
						int June = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "06");
						int July = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "07");
						int August = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "08");
						int September = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "09");
						int October = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "10");
						int November = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "11");
						int December = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "12");

						chartM.setData(FXCollections.observableArrayList(new PieChart.Data("1��", January),
								new PieChart.Data("2��", Febrary), new PieChart.Data("3��", March),
								new PieChart.Data("4��", April), new PieChart.Data("5��", May),
								new PieChart.Data("6��", June), new PieChart.Data("7��", July),
								new PieChart.Data("8��", August), new PieChart.Data("9��", September),
								new PieChart.Data("10��", October), new PieChart.Data("11��", November),
								new PieChart.Data("12��", December)));
					});
					// ����
					chartOut.setOnAction((e1) -> {
						int January1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "01");
						int Febrary1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "02");
						int March1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "03");
						int April1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "04");
						int May1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "05");
						int June1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "06");
						int July1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "07");
						int August1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "08");
						int September1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "09");
						int October1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "10");
						int November1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "11");
						int December1 = InOutcomeListDAO
								.getOutComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "12");

						chartM.setData(FXCollections.observableArrayList(new PieChart.Data("1��", January1),
								new PieChart.Data("2��", Febrary1), new PieChart.Data("3��", March1),
								new PieChart.Data("4��", April1), new PieChart.Data("5��", May1),
								new PieChart.Data("6��", June1), new PieChart.Data("7��", July1),
								new PieChart.Data("8��", August1), new PieChart.Data("9��", September1),
								new PieChart.Data("10��", October1), new PieChart.Data("11��", November1),
								new PieChart.Data("12��", December1)));
					});

					// ����-����
					chartInOut.setOnAction((e2) -> {
						int January2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "01")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "01");
						int Febrary2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "02")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "02");
						int March2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "03")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "03");
						int April2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "04")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "04");
						int May2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "05")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "05");
						int June2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "06")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "06");
						int July2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "07")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "07");
						int August2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "08")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "08");
						int September2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "09")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "09");
						int October2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "10")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "10");
						int November2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "11")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "11");
						int December2 = InOutcomeListDAO
								.getInComeDataOfYears(cmbYearGroup.getSelectionModel().getSelectedItem(), "12")
								- InOutcomeListDAO.getOutComeDataOfYears(
										cmbYearGroup.getSelectionModel().getSelectedItem(), "12");

						chartM.setData(FXCollections.observableArrayList(new PieChart.Data("1��", January2),
								new PieChart.Data("2��", Febrary2), new PieChart.Data("3��", March2),
								new PieChart.Data("4��", April2), new PieChart.Data("5��", May2),
								new PieChart.Data("6��", June2), new PieChart.Data("7��", July2),
								new PieChart.Data("8��", August2), new PieChart.Data("9��", September2),
								new PieChart.Data("10��", October2), new PieChart.Data("11��", November2),
								new PieChart.Data("12��", December2)));

					});
					// �з���
					chart2in.setOnAction((e3) -> {
						int X = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(), "����");
						int work = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"�ٷμҵ�");
						int finance = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"�����ҵ�");
						int gita = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"��Ÿ");

						chartC.setData(FXCollections.observableArrayList(new PieChart.Data("����", X),
								new PieChart.Data("�ٷμҵ�", work), new PieChart.Data("�����ҵ�", finance),
								new PieChart.Data("��Ÿ", gita)));
					});

					chart2out.setOnAction((e4) -> {
						int X1 = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(), "����");
						int eat = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(), "�ĺ�");
						int culture = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"��ȭ��Ȱ��");
						int home = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"�ְŻ�Ȱ��");
						int helth = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"�ǰ�������");
						int traffic = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"�����");
						int car = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"����������");
						int shopping = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"���κ�");
						int beauty = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"�̿��");
						int edu = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"������");
						int soc = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"��ȸ��Ȱ��");
						int kara = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"�����");
						int insu = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"���������");
						int pig = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(), "����");
						int gita1 = InOutcomeListDAO.Categorize(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"��Ÿ");

						chartC.setData(FXCollections.observableArrayList(new PieChart.Data("����", X1),
								new PieChart.Data("�ĺ�", eat), new PieChart.Data("��ȭ��Ȱ��", culture),
								new PieChart.Data("�ְŻ�Ȱ��", home), new PieChart.Data("�ǰ�������", helth),
								new PieChart.Data("�����", traffic), new PieChart.Data("����������", car),
								new PieChart.Data("���κ�", shopping), new PieChart.Data("�̿��", beauty),
								new PieChart.Data("������", edu), new PieChart.Data("��ȸ��Ȱ��", soc),
								new PieChart.Data("�����", kara), new PieChart.Data("���������", insu),
								new PieChart.Data("����", pig), new PieChart.Data("��Ÿ", gita1)));
					});
					// �������ܺ�
					chart3in.setOnAction((e5) -> {
						int X = InOutcomeListDAO.Payment0(cmbYearGroup.getSelectionModel().getSelectedItem(), "����");
						int work = InOutcomeListDAO.Payment0(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"�ٷμҵ�");
						int finance = InOutcomeListDAO.Payment0(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"�����ҵ�");
						int gita = InOutcomeListDAO.Payment0(cmbYearGroup.getSelectionModel().getSelectedItem(), "��Ÿ");

						chartP.setData(FXCollections.observableArrayList(new PieChart.Data("����", X),
								new PieChart.Data("�ٷμҵ�", work), new PieChart.Data("�����ҵ�", finance),
								new PieChart.Data("��Ÿ", gita)));
						int cash = InOutcomeListDAO.Payment0(cmbYearGroup.getSelectionModel().getSelectedItem(), "����");
						int check = InOutcomeListDAO.Payment0(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"üũī��");
						int sin = InOutcomeListDAO.Payment0(cmbYearGroup.getSelectionModel().getSelectedItem(), "�ſ�ī��");
						int gita2 = InOutcomeListDAO.Payment0(cmbYearGroup.getSelectionModel().getSelectedItem(), "��Ÿ");

						chartP.setData(FXCollections.observableArrayList(new PieChart.Data("����", cash),
								new PieChart.Data("üũī��", check), new PieChart.Data("�ſ�ī��", sin),
								new PieChart.Data("��Ÿ", gita2)));

					});
					chart3out.setOnAction((e6) -> {
						int X = InOutcomeListDAO.Payment1(cmbYearGroup.getSelectionModel().getSelectedItem(), "����");
						int work = InOutcomeListDAO.Payment1(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"�ٷμҵ�");
						int finance = InOutcomeListDAO.Payment1(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"�����ҵ�");
						int gita = InOutcomeListDAO.Payment1(cmbYearGroup.getSelectionModel().getSelectedItem(), "��Ÿ");

						chartP.setData(FXCollections.observableArrayList(new PieChart.Data("����", X),
								new PieChart.Data("�ٷμҵ�", work), new PieChart.Data("�����ҵ�", finance),
								new PieChart.Data("��Ÿ", gita)));
						int cash = InOutcomeListDAO.Payment1(cmbYearGroup.getSelectionModel().getSelectedItem(), "����");
						int check = InOutcomeListDAO.Payment1(cmbYearGroup.getSelectionModel().getSelectedItem(),
								"üũī��");
						int sin = InOutcomeListDAO.Payment1(cmbYearGroup.getSelectionModel().getSelectedItem(), "�ſ�ī��");
						int gita2 = InOutcomeListDAO.Payment1(cmbYearGroup.getSelectionModel().getSelectedItem(), "��Ÿ");

						chartP.setData(FXCollections.observableArrayList(new PieChart.Data("����", cash),
								new PieChart.Data("üũī��", check), new PieChart.Data("�ſ�ī��", sin),
								new PieChart.Data("��Ÿ", gita2)));

					});

					pieBtnClose.setOnAction((e) -> {
						IOStage.close();
					}); // ��Ҵ����� â�ݱ�

				}
			});

		} catch (Exception e) {
			callAlert("ȭ����ȯ����:ȭ����ȯ�� ������ �ֽ��ϴ�. ����ٶ�.");
		}
	}

	// 6. ���̺�� ���� ������ ->�ι������ϸ� ����
	private void selectTableListTwoClickRemove(MouseEvent e) {

		selectInOutcomeList = iolTableview.getSelectionModel().getSelectedItem();
		selectInOutcomeIndex = iolTableview.getSelectionModel().getSelectedIndex();

		if (e.getClickCount() == 2) {
			int count = InOutcomeListDAO.deleteInOutcomeListData(selectInOutcomeList.getNo());

			if (count != 0) {
				try {
					Stage IOStage = new Stage();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/notice.fxml"));
					Parent root = loader.load();
					Scene scene = new Scene(root);
					IOStage.initModality(Modality.WINDOW_MODAL);
					IOStage.initOwner(mainStage);
					IOStage.setScene(scene);
					IOStage.setTitle("Will you delete it?");
					IOStage.setResizable(false);
					IOStage.show();

					Button btnNDelete = (Button) root.lookup("#btnNDelete");// ������ư
					Button btnNCancel = (Button) root.lookup("#btnNCancel");// ��ҹ�ư

					// ����
					btnNDelete.setOnAction((ActionEvent event) -> {
						iolData.remove(selectInOutcomeIndex);
						iolTableview.refresh();
						callAlert("�����Ϸ� : ��������");
						IOStage.close();
					});

					// ���
					btnNCancel.setOnAction((ActionEvent event) -> {
						IOStage.close();
					});

				} catch (Exception e1) {
					callAlert("ȭ����ȯ����:ȭ����ȯ�� ������ �ֽ��ϴ�. ����ٶ�.");
				}

			} else {
				callAlert("�������� : Ȯ�ο��");
			}
		} // end of if
	}

	// ***�˸�â***
	public static void callAlert(String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Notice");
		alert.setHeaderText(contentText.substring(0, contentText.lastIndexOf(":")));
		alert.setContentText(contentText.substring(contentText.lastIndexOf(":") + 1));
		alert.showAndWait();
	}
}
