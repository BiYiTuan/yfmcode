import shidian.tv.sntv.tools.DbUtils;
import shidian.tv.sntv.tools.Result;
import android.test.AndroidTestCase;


public class Test extends AndroidTestCase {

	public void testLogin() throws Exception {  
		Result rt=new Result();
		rt.setPhone("13473405826");
		rt.setState("δ��ȡ");
		rt.setTmsga("лл����");
		rt.setTmsgb("ʳ����");
		rt.setGiftname("");
		DbUtils.getInstance(this.getContext()).addGift(rt);
    }  
}
