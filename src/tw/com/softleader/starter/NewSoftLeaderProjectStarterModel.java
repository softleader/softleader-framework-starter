package tw.com.softleader.starter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tw.com.softleader.starter.files.F;

public class NewSoftLeaderProjectStarterModel implements Collection<F> {

	private ArrayList<F> delegate = new ArrayList<>();

	public NewSoftLeaderProjectStarterModel(String URL)
			throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		super();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(URL);
		doc.getDocumentElement().normalize();
		NodeList nodes = doc.getElementsByTagName("f");
		IntStream.range(0, nodes.getLength()).parallel().forEach(i -> {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) node;
				F f = new F(ele.getAttribute("n"), ele.getAttribute("p"), ele.getAttribute("d"));
				String url = ele.getAttribute("u");
				if (url != null && !url.isEmpty()) {
					try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
						f.setContent(buffer.lines().collect(Collectors.joining("\n")));
					} catch (Exception e) {
						throw new Error(e);
					}
				}
				this.add(f);
			}
		});
	}

	public void trimToSize() {
		delegate.trimToSize();
	}

	public void ensureCapacity(int minCapacity) {
		delegate.ensureCapacity(minCapacity);
	}

	public int size() {
		return delegate.size();
	}

	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	public int indexOf(Object o) {
		return delegate.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return delegate.lastIndexOf(o);
	}

	public Object clone() {
		return delegate.clone();
	}

	public Object[] toArray() {
		return delegate.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}

	public String toString() {
		return delegate.toString();
	}

	public F get(int index) {
		return delegate.get(index);
	}

	public F set(int index, F element) {
		return delegate.set(index, element);
	}

	public boolean add(F e) {
		return delegate.add(e);
	}

	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	public void add(int index, F element) {
		delegate.add(index, element);
	}

	public F remove(int index) {
		return delegate.remove(index);
	}

	public boolean remove(Object o) {
		return delegate.remove(o);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public void clear() {
		delegate.clear();
	}

	public boolean addAll(Collection<? extends F> c) {
		return delegate.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends F> c) {
		return delegate.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}

	public ListIterator<F> listIterator(int index) {
		return delegate.listIterator(index);
	}

	public ListIterator<F> listIterator() {
		return delegate.listIterator();
	}

	public Iterator<F> iterator() {
		return delegate.iterator();
	}

	public List<F> subList(int fromIndex, int toIndex) {
		return delegate.subList(fromIndex, toIndex);
	}

	public void forEach(Consumer<? super F> action) {
		delegate.forEach(action);
	}

	public Spliterator<F> spliterator() {
		return delegate.spliterator();
	}

	public boolean removeIf(Predicate<? super F> filter) {
		return delegate.removeIf(filter);
	}

	public void replaceAll(UnaryOperator<F> operator) {
		delegate.replaceAll(operator);
	}

	public void sort(Comparator<? super F> c) {
		delegate.sort(c);
	}

}
