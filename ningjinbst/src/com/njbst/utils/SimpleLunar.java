package com.njbst.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class SimpleLunar {
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy��MM��dd��");

	/** ��Сʱ��1900-1-31*/
	private final static long minTimeInMillis = -2206425952001L;
	/** ���ʱ��2099-12-31 */
	private final static long maxTimeInMillis = 4102416000000L;
	/**
	 * ũ�������ݱ�(1900-2099��)<br>
	 * <br>
	 * ÿ��ũ������16��������ʾ������ʱתΪ2����<br>
	 * ǰ12λ�ֱ��ʾ12��ũ���·ݵĴ�С�£�1�Ǵ��£�0��С��<br>
	 * ���4λ��ʾ���£�תΪʮ���ƺ�Ϊ����ֵ������0110����Ϊ��6��
	 */
	private final static int[] lunarInfo = { 0x4bd8, 0x4ae0, 0xa570, 0x54d5, 0xd260, 0xd950, 0x5554, 0x56af, 0x9ad0, 0x55d2, 0x4ae0, 0xa5b6, 0xa4d0, 0xd250, 0xd295, 0xb54f, 0xd6a0, 0xada2, 0x95b0,
			0x4977, 0x497f, 0xa4b0, 0xb4b5, 0x6a50, 0x6d40, 0xab54, 0x2b6f, 0x9570, 0x52f2, 0x4970, 0x6566, 0xd4a0, 0xea50, 0x6a95, 0x5adf, 0x2b60, 0x86e3, 0x92ef, 0xc8d7, 0xc95f, 0xd4a0, 0xd8a6,
			0xb55f, 0x56a0, 0xa5b4, 0x25df, 0x92d0, 0xd2b2, 0xa950, 0xb557, 0x6ca0, 0xb550, 0x5355, 0x4daf, 0xa5b0, 0x4573, 0x52bf, 0xa9a8, 0xe950, 0x6aa0, 0xaea6, 0xab50, 0x4b60, 0xaae4, 0xa570,
			0x5260, 0xf263, 0xd950, 0x5b57, 0x56a0, 0x96d0, 0x4dd5, 0x4ad0, 0xa4d0, 0xd4d4, 0xd250, 0xd558, 0xb540, 0xb6a0, 0x95a6, 0x95bf, 0x49b0, 0xa974, 0xa4b0, 0xb27a, 0x6a50, 0x6d40, 0xaf46,
			0xab60, 0x9570, 0x4af5, 0x4970, 0x64b0, 0x74a3, 0xea50, 0x6b58, 0x5ac0, 0xab60, 0x96d5, 0x92e0, 0xc960, 0xd954, 0xd4a0, 0xda50, 0x7552, 0x56a0, 0xabb7, 0x25d0, 0x92d0, 0xcab5, 0xa950,
			0xb4a0, 0xbaa4, 0xad50, 0x55d9, 0x4ba0, 0xa5b0, 0x5176, 0x52bf, 0xa930, 0x7954, 0x6aa0, 0xad50, 0x5b52, 0x4b60, 0xa6e6, 0xa4e0, 0xd260, 0xea65, 0xd530, 0x5aa0, 0x76a3, 0x96d0, 0x4afb,
			0x4ad0, 0xa4d0, 0xd0b6, 0xd25f, 0xd520, 0xdd45, 0xb5a0, 0x56d0, 0x55b2, 0x49b0, 0xa577, 0xa4b0, 0xaa50, 0xb255, 0x6d2f, 0xada0, 0x4b63, 0x937f, 0x49f8, 0x4970, 0x64b0, 0x68a6, 0xea5f,
			0x6b20, 0xa6c4, 0xaaef, 0x92e0, 0xd2e3, 0xc960, 0xd557, 0xd4a0, 0xda50, 0x5d55, 0x56a0, 0xa6d0, 0x55d4, 0x52d0, 0xa9b8, 0xa950, 0xb4a0, 0xb6a6, 0xad50, 0x55a0, 0xaba4, 0xa5b0, 0x52b0,
			0xb273, 0x6930, 0x7337, 0x6aa0, 0xad50, 0x4b55, 0x4b6f, 0xa570, 0x54e4, 0xd260, 0xe968, 0xd520, 0xdaa0, 0x6aa6, 0x56df, 0x4ae0, 0xa9d4, 0xa4d0, 0xd150, 0xf252, 0xd520 };
	/** ʮ����Ф */
	private final static String[] Animals = { "��", "ţ", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��" };
	/** ũ�������ַ���һ */
	private final static String[] lunarString1 = { "��", "һ", "��", "��", "��", "��", "��", "��", "��", "��" };
	/** ũ�������ַ����� */
	private final static String[] lunarString2 = { "��", "ʮ", "إ", "ئ", "��", "��", "��", "��" };
	/** ũ���� */
	private int lunarYear;
	/** ũ���� */
	private int lunarMonth;
	/** ũ���� */
	private int lunarDay;
	/** �Ƿ������� */
	private boolean isLeap;
	/** �Ƿ������� */
	private boolean isLeapYear;
	/** ĳũ���µ�������� */
	private int maxDayInMonth = 29;

	/**
	 * ͨ�� TimeInMillis ����ũ����Ϣ
	 * @param TimeInMillis
	 */
	public SimpleLunar(long TimeInMillis) {
		this.init(TimeInMillis);
	}

	/**
	 * ͨ�� Date ���󹹽�ũ����Ϣ
	 * @param date ָ�����ڶ���
	 */
	public SimpleLunar(Date date) {
		if (date == null)
			date = new Date();
		this.init(date.getTime());
	}

	/**
	 * ũ����ʼ��
	 * @param timeInMillis ʱ�������
	 */
	private void init(long timeInMillis) {
		if (timeInMillis > minTimeInMillis && timeInMillis < maxTimeInMillis) {
			// ��ũ��Ϊ1900������һ�յ�1900-1-31��Ϊ��ʼ����
			Calendar baseDate = new GregorianCalendar(1900, 0, 31);
			// ������ʼ���ڼ����������
			long offset = (timeInMillis - baseDate.getTimeInMillis()) / 86400000;
			// Ĭ��ũ����Ϊ1900�꣬���ɴ˿�ʼ����ũ�����
			this.lunarYear = 1900;
			int daysInLunarYear = SimpleLunar.getLunarYearDays(this.lunarYear);
			// �ݼ�ÿ��ũ�������������ȷ��ũ�����
			while (this.lunarYear < 2100 && offset >= daysInLunarYear) {
				offset -= daysInLunarYear;
				daysInLunarYear = SimpleLunar.getLunarYearDays(++this.lunarYear);
			}
			// ��ȡ��ũ����������·�
			int leapMonth = SimpleLunar.getLunarLeapMonth(this.lunarYear);
			// û��������������
			this.isLeapYear = leapMonth > 0;

			// Ĭ��ũ����Ϊ���£����ɴ˿�ʼ�Ƽ�ũ����
			int lunarMonth = 1;
			// �Ƿ�ݼ�ũ����
			boolean isDecrease = true;
			boolean isLeap = false;
			int daysInLunarMonth = 0;
			// �ݼ�ÿ��ũ���µ���������ȷ��ũ���·�
			while (lunarMonth < 13 && offset > 0) {
				if (isLeap && !isDecrease) {
					// ��ũ�������µ�������
					daysInLunarMonth = SimpleLunar.getLunarLeapDays(this.lunarYear);
					isDecrease = true;
				} else {
					// ��ũ��������ũ���·ݵ�����
					daysInLunarMonth = SimpleLunar.getLunarMonthDays(this.lunarYear, lunarMonth);
				}
				if (offset < daysInLunarMonth) {
					break;
				}
				offset -= daysInLunarMonth;

				// ���ũ���������£��򲻵���ũ���·�
				if (leapMonth == lunarMonth && isLeap == false) {
					isDecrease = false;
					isLeap = true;
				} else {
					lunarMonth++;
				}
			}
			// ���daysInLunarMonthΪ0��˵��Ĭ��ũ���¼�Ϊ���ص�ũ����
			this.maxDayInMonth = daysInLunarMonth != 0 ? daysInLunarMonth : SimpleLunar.getLunarMonthDays(this.lunarYear, lunarMonth);
			this.lunarMonth = lunarMonth;
			this.isLeap = (lunarMonth == leapMonth && isLeap);
			this.lunarDay = (int) offset + 1;
		}
	}

	/**
	 * ��ȡĳũ�����������
	 * @param lunarYear ũ�����
	 * @return ��ũ�����������
	 */
	private static int getLunarYearDays(int lunarYear) {
		// ��С�¼���,ũ����������12 * 29 = 348��
		int daysInLunarYear = 348;

		// ����ǰ12λ
		for (int i = 0x8000; i > 0x8; i >>= 1) {
			// ÿ�������ۼ�һ��
			daysInLunarYear += ((SimpleLunar.lunarInfo[lunarYear - 1900] & i) != 0) ? 1 : 0;
		}
		// ������������
		daysInLunarYear += SimpleLunar.getLunarLeapDays(lunarYear);

		return daysInLunarYear;
	}

	/**
	 * ��ȡĳũ�������µ�������
	 * @param lunarYear ũ�����
	 * @return ��ũ�������µ���������û�����·���0
	 */
	private static int getLunarLeapDays(int lunarYear) {
		// ��һ�����4bitΪ1111,����30(����)
		// ��һ�����4bit��Ϊ1111,����29(С��)
		// ������û������,����0
		return SimpleLunar.getLunarLeapMonth(lunarYear) > 0 ? ((SimpleLunar.lunarInfo[lunarYear - 1899] & 0xf) == 0xf ? 30 : 29) : 0;
	}

	/**
	 * ��ȡĳũ���������·�
	 * @param lunarYear ũ�����
	 * @return ��ũ�������µ��·ݣ�û�����·���0
	 */
	private static int getLunarLeapMonth(int lunarYear) {
		// ƥ���4λ
		int leapMonth = SimpleLunar.lunarInfo[lunarYear - 1900] & 0xf;
		// �����4λȫΪ1��ȫΪ0,��ʾû��
		leapMonth = (leapMonth == 0xf ? 0 : leapMonth);
		return leapMonth;
	}

	/**
	 * ��ȡĳũ����ĳũ���·ݵ�������
	 * @param lunarYear ũ�����
	 * @param lunarMonth ũ���·�
	 * @return ��ũ�����ũ���µ�������
	 */
	private static int getLunarMonthDays(int lunarYear, int lunarMonth) {
		// ƥ��ǰ12λ�������Ӧũ���·ݵĴ�С�£�����30�죬С��29��
		int daysInLunarMonth = ((SimpleLunar.lunarInfo[lunarYear - 1900] & (0x10000 >> lunarMonth)) != 0) ? 30 : 29;
		return daysInLunarMonth;
	}

	/**
	 * ����ָ�����ֵ�ũ����ݱ�ʾ�ַ���
	 * @param lunarYear ũ�����(����,0Ϊ����)
	 * @return ũ������ַ���
	 */
	private static String getLunarYearString(int lunarYear) {
		String lunarYearString = "";
		String year = String.valueOf(lunarYear);
		for (int i = 0; i < year.length(); i++) {
			char yearChar = year.charAt(i);
			int index = Integer.parseInt(String.valueOf(yearChar));
			lunarYearString += lunarString1[index];
		}
		return lunarYearString;
	}

	/**
	 * ����ָ�����ֵ�ũ���·ݱ�ʾ�ַ���
	 * @param lunarMonth ũ���·�(����)
	 * @return ũ���·��ַ��� (��:��)
	 */
	private static String getLunarMonthString(int lunarMonth) {
		String lunarMonthString = "";
		if (lunarMonth == 1) {
			lunarMonthString = SimpleLunar.lunarString2[4];
		} else {
			if (lunarMonth > 9)
				lunarMonthString += SimpleLunar.lunarString2[1];
			if (lunarMonth % 10 > 0)
				lunarMonthString += SimpleLunar.lunarString1[lunarMonth % 10];
		}
		return lunarMonthString;
	}

	/**
	 * ����ָ�����ֵ�ũ���ձ�ʾ�ַ���
	 * @param lunarDay ũ����(����)
	 * @return ũ�����ַ��� (��: إһ)
	 */
	private static String getLunarDayString(int lunarDay) {
		if (lunarDay < 1 || lunarDay > 30)
			return "";
		int i1 = lunarDay / 10;
		int i2 = lunarDay % 10;
		String c1 = SimpleLunar.lunarString2[i1];
		String c2 = SimpleLunar.lunarString1[i2];
		if (lunarDay < 11)
			c1 = SimpleLunar.lunarString2[0];
		if (i2 == 0)
			c2 = SimpleLunar.lunarString2[1];
		return c1 + c2;
	}

	/**
	 * ȡũ������Ф
	 * @return ũ������Ф(��:��)
	 */
	public String getAnimalString() {
		if (lunarYear == 0)
			return null;
		return SimpleLunar.Animals[(this.lunarYear - 4) % 12];
	}

	/**
	 * ����ũ�������ַ���
	 * @return ũ�������ַ���
	 */
	public String getDayString() {
		if (lunarDay == 0)
			return null;
		return SimpleLunar.getLunarDayString(this.lunarDay);
	}

	/**
	 * ����ũ�������ַ���
	 * @return ũ�������ַ���
	 */
	public String getMonthString() {
		if (lunarMonth == 0)
			return null;
		return (this.isLeap() ? "��" : "") + SimpleLunar.getLunarMonthString(this.lunarMonth);
	}

	/**
	 * ����ũ�������ַ���
	 * @return ũ�������ַ���
	 */
	public String getYearString() {
		if (lunarYear == 0)
			return null;
		return SimpleLunar.getLunarYearString(this.lunarYear);
	}

	/**
	 * ����ũ����ʾ�ַ���
	 * @return ũ���ַ���(��:���������³���)
	 */
	public String getDateString() {
		if (lunarYear == 0)
			return null;
		return this.getYearString() + "��" + this.getMonthString() + "��" + this.getDayString() + "��";
	}

	/**
	 * ũ�����Ƿ�������
	 * @return ũ�����Ƿ�������
	 */
	public boolean isLeap() {
		return isLeap;
	}

	/**
	 * ũ�����Ƿ�������
	 * @return ũ�����Ƿ�������
	 */
	public boolean isLeapYear() {
		return isLeapYear;
	}

	/**
	 * ��ǰũ�����Ƿ��Ǵ���
	 * @return ��ǰũ�����Ǵ���
	 */
	public boolean isBigMonth() {
		return this.getMaxDayInMonth() > 29;
	}

	/**
	 * ��ǰũ�����ж�����
	 * @return ����
	 */
	public int getMaxDayInMonth() {
		if (lunarYear == 0)
			return 0;
		return this.maxDayInMonth;
	}

	/**
	 * ũ������
	 * @return ũ������
	 */
	public int getDay() {
		return lunarDay;
	}

	/**
	 * ũ���·�
	 * @return ũ���·�
	 */
	public int getMonth() {
		return lunarMonth;
	}

	/**
	 * ũ�����
	 * @return ũ�����
	 */
	public int getYear() {
		return lunarYear;
	}
	
	public static String getHomeTime(){
		Date d=new Date();
		SimpleLunar sc=new SimpleLunar(d);
		String time=sc.sdf.format(d)+" "+"ũ��"+sc.getMonthString()+"��"+sc.getDayString()+"��";
		return time;
	}
	
	
}