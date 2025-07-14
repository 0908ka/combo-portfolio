import logo from './logo.svg';
import './App.css';
import {useState, useEffect} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css'

//npm start ← react開始

function App() {
  const[combos, setCombos] = useState([]);
  const[character, setCharacter] = useState('');
  const[combo, setCombo] = useState('');
  const[editId, setEditId] = useState(null);
  const[searchKeyword, setSearchKeyword] = useState('');
  const[sortAsc, setSortAsc] = useState(true);

  useEffect(() => {
    fetchCombos();
  },[]);

  const fetchCombos = () => {
    fetch('http://localhost:8080/api/combos',{cache: 'no-store'})
    .then(response => response.json())
    .then(data => {
      console.log("取得した一覧データ:", data);
    setCombos(data)})
    .catch(error => console.error('取得エラー:', error));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("追加処理実行開始");
    if(!character.trim() || !combo.trim()){
      alert("キャラ名とコンボの内容の両方を入力してください");
      return;
    }

    const comboData = editId
      ? {id: editId, character , combo}   //更新用（id付）
      : {character, combo};               //新規追加用

    const method = editId ? 'PUT' : 'POST';

    const url = editId
      ? `http://localhost:8080/api/combos/${editId}`
      : `http://localhost:8080/api/combos`;

    fetch(url,{
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({character, combo})
    })
      .then(response => {
        if(!response.ok)throw new Error('登録/更新に失敗しました');
        return response.json();
      })
      .then((returnedCombo) => {
        console.log("サーバーからの返却:", returnedCombo);
        if(editId){
          //編集の場合→配列内の該当要素を置き換える
          setCombos(prevCombos =>
            prevCombos.map(c => (c.id === returnedCombo.id ? returnedCombo : c))
          );
        }else{
          //新規追加の場合→ 配列末尾スイカ
          setCombos(prevCombos =>[...prevCombos, returnedCombo]);
        }

        setCharacter('');
        setCombo('');
        setEditId(null);
      })
      .catch(error => console.error('送信エラー:', error));
  };

  const handleDelete = (id) => {
    fetch(`http://localhost:8080/api/combos/${id}`,{
      method: 'DELETE',
    })
      .then(() => {
        //削除後に状態を更新
        fetchCombos();
      })
      .catch(error => console.error('削除エラー:', error));
  };

  const filteredCombos = combos
  .filter(c => c.character.toLowerCase().includes(searchKeyword.toLowerCase()))
  .sort((a, b) => {
      if(sortAsc) {
        return a.id - b.id;
      }else{
        return b.id -a.id;
      }
  });

  return (
    <div className="container mt-4">
      <h1 className="mb-4">コンボメモアプリ</h1>

      <form onSubmit={handleSubmit} className="mb-4">
        <div className="row g-2 align-item-center">
        <div className="col-md-4">
        <input
          type='text'
          className="form-control"
          placeholder='キャラ名'
          value={character}
          onChange={(e) => setCharacter(e.target.value)}
          required
        />
        </div>
        <div className="col-md-3">
        <input
          type='text'
          className="form-control"
          placeholder='コンボ'
          value={combo}
          onChange={(e) => setCombo(e.target.value)}
          required
        />
        </div>
        <div className="col-md-3">
        <button type='submit' className="btn btn-primary w-100" disabled={!character.trim() || !combo.trim()}>{editId ? '更新' : '追加'}</button>
        </div>
        </div>
      </form>

      <div className="d-flex justify-content-between align-items-center mb-3">
        <input type="text"
                className="form-control w-50"
                placeholder="キャラ名で検索"
                value={searchKeyword}
                onChange={(e) => setSearchKeyword(e.target.value)}
        />
        <button
          className="btn btn-outline-secondary ms-2"
          onClick={() => setSortAsc(!sortAsc)}>
          {sortAsc ? "新→旧" : "旧→新"}
        </button>
      </div>
      
      <ul className="list-group">
        {filteredCombos.map((combo) => (
          <li key={combo.id} className="list-group-item d-flex justify-content-between align-items-center">
            <div>
            <strong>{combo.character}</strong>:{combo.combo}
            </div>
            <div>
            <button className="btn btn-primary btn-sm" 
              onClick={() => {
              setCharacter(combo.character);
              setCombo(combo.combo);
              setEditId(combo.id);
            }}>編集</button>
            <button className="btn btn-danger btn-sm" 
              onClick={() => {
                if (window.confirm("本当に削除してもよろしいですか？"))handleDelete(combo.id)}}>削除</button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
