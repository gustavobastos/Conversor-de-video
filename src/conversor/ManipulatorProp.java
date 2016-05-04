package conversor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Gustavo Bastos
 * 
 *         //Classe responsável pela manipulação do arquivo de propriedades
 */

public class ManipulatorProp {

	public ManipulatorProp() {

	}

	public Properties getPro() throws IOException {
		Properties props = new Properties();
		InputStream file = getClass().getClassLoader().getResourceAsStream("dados.propriedades");
		props.load(file);
		return props;

	}
}
